package com.junyounggoat.dreamstore.userservice.service;

import com.junyounggoat.dreamstore.userservice.constant.UserLoginCategoryCode;
import com.junyounggoat.dreamstore.userservice.dto.*;
import com.junyounggoat.dreamstore.userservice.entity.*;
import com.junyounggoat.dreamstore.userservice.repository.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Builder
    @Getter
    private static class CreateMemberCreatedEntity {
        private User user;
        private UserLoginCategory userLoginCategory;
        private List<UserAgreementItem> userAgreementItemList;
        private UserPrivacyUsagePeriod userPrivacyUsagePeriod;
    }

    public TokenResponseDTO createUserByLoginCredentials(CreateUserRequestDTO createUserRequestDTO) {
        CreateMemberCreatedEntity createMemberCreatedEntity = createMember(createUserRequestDTO, UserLoginCategoryCode.userLoginCredentials);

        createUserLoginCredentials(createUserRequestDTO.getUserLoginCredentials()
                .toUserLoginCredentialsBuilder(passwordEncoder)
                .userLoginCategory(createMemberCreatedEntity.getUserLoginCategory())
                .build());

        return tokenService.createAccessTokenWithRefreshToken(createMemberCreatedEntity.getUser().getUserId());
    }

    // 회원 가입 시 공통 프로세스
    private CreateMemberCreatedEntity createMember(CreateUserRequestDTO createUserRequestDTO, UserLoginCategoryCode userLoginCategoryCode) {
        // user 엔티티
        User createdUser = userRepository.insertUser(
                createUserRequestDTO.getUser()
                        .toUserBuilder()
                        .userNonmemberFlag(false)
                        .build()
        );
        // userLoginCategory 엔티티
        UserLoginCategory createdUserLoginCategory = userRepository.insertUserLoginCategory(
                UserLoginCategory.builder()
                        .user(createdUser)
                        .userLoginCategoryCode(userLoginCategoryCode.getCode())
                        .build()
        );
        // userAgreementItem 엔티티 - 여러개
        List<UserAgreementItem> createdUserAgreementItemList = userRepository.insertUserAgreementItems(
                createdUser,
                createUserRequestDTO.getUserAgreementItemCodeList()
        );
        // userPrivacyUsagePeriod 엔티티
        UserPrivacyUsagePeriod createdUserPrivacyUsagePeriod = userRepository.insertUserPrivacyUsagePeriod(
                createdUser,
                createUserRequestDTO.getUserPrivacyUsagePeriodCode()
        );

        return CreateMemberCreatedEntity
                .builder()
                .user(createdUser)
                .userLoginCategory(createdUserLoginCategory)
                .userAgreementItemList(createdUserAgreementItemList)
                .userPrivacyUsagePeriod(createdUserPrivacyUsagePeriod)
                .build();
    }

    private void createUserLoginCredentials(UserLoginCredentials userLoginCredentials) {
        userRepository.insertUserLoginCredentials(userLoginCredentials);
    }

    public @Nullable TokenResponseDTO login(String loginUserName, String rawLoginUserPassword) {
        UserLoginCredentials userLoginCredentials = userRepository.findUserLoginCredentialsByLoginUserName(loginUserName);
        if (userLoginCredentials == null) {
            return null;
        }

        if (!passwordEncoder.matches(rawLoginUserPassword, userLoginCredentials.getLoginUserPassword())) {
            return null;
        }

        return tokenService.createAccessTokenWithRefreshToken(userLoginCredentials.getUserLoginCategory().getUser().getUserId());
    }

    @Transactional(readOnly = true)
    public @Nullable MyUserDTO getMyUser(Long userId) {
        User user = userRepository.findUserByUserId(userId);

        if (user == null) {
            return null;
        }

        return MyUserDTO.builder()
                .user(MyUserDTO.MyUserDTOUser.builder().user(user).build())
                .userPrivacyUsagePeriod(MyUserDTO.UserPrivacyUsagePeriodDTO.builder()
                        .userPrivacyUsagePeriod(user.getUserPrivacyUsagePeriod())
                        .build())
                .userLoginCategoryList(user.getUserLoginCategoryList()
                        .stream()
                        .map(userLoginCategory -> MyUserDTO.UserLoginCategoryDTO.builder()
                                .userLoginCategory(userLoginCategory).build()).toList())
                .userAgreementItemList(user.getUserAgreementItemList()
                        .stream()
                        .map(userAgreementItem -> MyUserDTO.UserAgreementItemDTO.builder()
                                .userAgreementItem(userAgreementItem).build()).toList())
                .build();
    }

    @Transactional(readOnly = true)
    public @Nullable OtherUserDTO getOtherUser(long userId) {
        User user = userRepository.findUserByUserId(userId);
        if (user == null) {
            return null;
        }

        return OtherUserDTO.builder()
                .user(user)
                .build();
    }

    public @Nullable UpdateMyUserResponseDTO updateMyUser(long userId, UpdateMyUserRequestDTO updateMyUserRequestDTO) {
        User currentUser = userRepository.findUserByUserId(userId);
        if (currentUser == null) {
            return null;
        }

        User updatedUser = userRepository.updateUser(updateMyUserRequestDTO.updateUser(currentUser));

        return UpdateMyUserResponseDTO.builder()
                .user(updatedUser)
                .build();
    }
}
