package com.junyounggoat.dreamstore.userservice.service;

import com.junyounggoat.dreamstore.userservice.constant.UserLoginCategoryCode;
import com.junyounggoat.dreamstore.userservice.dto.CreateUserRequestDTO;
import com.junyounggoat.dreamstore.userservice.dto.CreateUserResponseDTO;
import com.junyounggoat.dreamstore.userservice.entity.*;
import com.junyounggoat.dreamstore.userservice.repository.UserRepository;
import com.junyounggoat.dreamstore.userservice.util.JwtUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
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

    public CreateUserResponseDTO createUserByLoginCredentials(CreateUserRequestDTO createUserRequestDTO) {
        CreateMemberCreatedEntity createMemberCreatedEntity = createMember(createUserRequestDTO, UserLoginCategoryCode.userLoginCredentials);

        createUserLoginCredentials(createUserRequestDTO.getUserLoginCredentials()
                .toUserLoginCredentialsBuilder(passwordEncoder)
                .userLoginCategory(createMemberCreatedEntity.getUserLoginCategory())
                .build());

        return CreateUserResponseDTO.builder()
                .accessToken(JwtUtil.createAccessToken(createMemberCreatedEntity.getUser().getUserId()))
                .build();
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
}
