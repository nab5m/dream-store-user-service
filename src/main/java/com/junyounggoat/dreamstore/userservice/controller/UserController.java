package com.junyounggoat.dreamstore.userservice.controller;

import com.junyounggoat.dreamstore.userservice.constant.CodeCategoryName;
import com.junyounggoat.dreamstore.userservice.dto.*;
import com.junyounggoat.dreamstore.userservice.service.KakaoLoginService;
import com.junyounggoat.dreamstore.userservice.service.NaverLoginService;
import com.junyounggoat.dreamstore.userservice.validation.*;
import com.junyounggoat.dreamstore.userservice.repository.CodeRepository.CodeCategoryNameAndCodeName;
import com.junyounggoat.dreamstore.userservice.service.UserService;
import com.junyounggoat.dreamstore.userservice.swagger.UserControllerDocs;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

import static com.junyounggoat.dreamstore.userservice.service.TokenService.getUserIdFromUserDetails;
import static com.junyounggoat.dreamstore.userservice.validation.RequiredUserAgreementItemValidator.validateRequiredUserAgreementItem;
import static com.junyounggoat.dreamstore.userservice.validation.UnAuthorizedException.LOGIN_REQUIRED_ERROR_MESSAGE;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "사용자 컨트롤러")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    public static final String USER_NOT_FOUND_MESSAGE = "존재하지 않는 사용자입니다.";
    public static final String LOGIN_USER_NOT_VALID_MESSAGE = "아이디나 비밀번호가 일치하지 않습니다.";

    private final UserService userService;
    private final KakaoLoginService kakaoLoginService;
    private final NaverLoginService naverLoginService;
    private final UniqueColumnValidator uniqueColumnValidator;
    private final RequiredUserAgreementItemValidator requiredUserAgreementItemValidator;
    private final CodeExistValidator codeExistValidator;

    private void validateNewMemberCommonFields(NewMemberCommonFields newMemberCommonFields, Errors errors) {
        UniqueColumnValidator.validateUniqueUserEmailAddress(uniqueColumnValidator,
                "user.userEmailAddress",
                newMemberCommonFields.getUser().getUserEmailAddress(),
                errors);
        UniqueColumnValidator.validateUniqueUserPhoneNumber(uniqueColumnValidator,
                "user.userPhoneNumber",
                newMemberCommonFields.getUser().getUserPhoneNumber(),
                errors);

        // 이렇게 검증이 필요하다면 그냥 fk를 걸었지 싶다
        List<CodeExistValidator.TargetCodeItem> usedCodeList = new LinkedList<>();
        newMemberCommonFields.getUserAgreementItemCodeList().forEach((userAgreementItemCode) -> {
            usedCodeList.add(CodeExistValidator.TargetCodeItem.builder()
                    .codeItem(CodeCategoryNameAndCodeName.builder()
                            .codeCategoryName(CodeCategoryName.USER_AGREEMENT_ITEM.getName())
                            .code(userAgreementItemCode)
                            .build())
                    .field("userAgreementItemCodeList")
                    .build());
        });

        usedCodeList.add(CodeExistValidator.TargetCodeItem.builder()
                .codeItem(CodeCategoryNameAndCodeName.builder()
                        .codeCategoryName(CodeCategoryName.USER_PRIVACY_USAGE_PERIOD.getName())
                        .code(newMemberCommonFields.getUserPrivacyUsagePeriodCode())
                        .build())
                .field("userPrivacyUsagePeriodCode")
                .build());

        CodeExistValidator.validateCodeExists(codeExistValidator, usedCodeList, errors);
        validateRequiredUserAgreementItem(requiredUserAgreementItemValidator,
                "userAgreementItemCodeList",
                newMemberCommonFields.getUserAgreementItemCodeList(),
                errors);
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    @UserControllerDocs.CreateUserDocs
    public TokenResponseDTO createUser(@RequestBody @Valid CreateUserRequestDTO createUserRequestDTO, Errors errors) {
        NotValidException.throwIfErrorExists(errors);

        validateNewMemberCommonFields(createUserRequestDTO, errors);
        UniqueColumnValidator.validateUniqueLoginUserName(uniqueColumnValidator,
                "userLoginCredentials.loginUserName",
                createUserRequestDTO.getUserLoginCredentials().getLoginUserName(),
                errors);

        NotValidException.throwIfErrorExists(errors);

        return userService.createUserByLoginCredentials(createUserRequestDTO);
    }

    @PostMapping("/kakao")
    @ResponseStatus(code = HttpStatus.CREATED)
    @UserControllerDocs.CreateKakaoUserDocs
    public TokenResponseDTO createKakaoUser(@RequestBody @Valid CreateKakaoUserRequestDTO createKakaoUserRequestDTO, Errors errors) {
        NotValidException.throwIfErrorExists(errors);

        validateNewMemberCommonFields(createKakaoUserRequestDTO, errors);
        UniqueColumnValidator.validateUniqueKakaoId(uniqueColumnValidator, "kakaoId", createKakaoUserRequestDTO.getKakaoId(), errors);
        NotValidException.throwIfErrorExists(errors);

        return userService.createKakaoUser(createKakaoUserRequestDTO);
    }

    @PostMapping("/naver")
    @ResponseStatus(code = HttpStatus.CREATED)
    @UserControllerDocs.CreateNaverUserDocs
    public TokenResponseDTO createNaverUser(@RequestBody @Valid CreateNaverUserRequestDTO createNaverUserRequestDTO, Errors errors) {
        NotValidException.throwIfErrorExists(errors);

        validateNewMemberCommonFields(createNaverUserRequestDTO, errors);
        UniqueColumnValidator.validateUniqueNaverId(uniqueColumnValidator, "naverAccessToken", createNaverUserRequestDTO.getNaverAccessToken(), errors);
        NotValidException.throwIfErrorExists(errors);

        return userService.createNaverUser(createNaverUserRequestDTO);
    }

    @PostMapping("/login")
    @UserControllerDocs.LoginDocs
    public TokenResponseDTO login(@RequestBody @Valid LoginRequestDTO loginRequestDTO, Errors errors) {
        NotValidException.throwIfErrorExists(errors);

        TokenResponseDTO response = userService.login(loginRequestDTO.getLoginUserName(), loginRequestDTO.getRawLoginUserPassword());

        if (response == null) {
            errors.reject("IncorrectLoginUserNameOrLoginUserPassword", LOGIN_USER_NOT_VALID_MESSAGE);
            throw NotValidException.builder().errors(errors).build();
        }

        return response;
    }

    @GetMapping("/mine")
    @UserControllerDocs.GetMyUserDocs
    public MyUserDTO getMyUser(@AuthenticationPrincipal UserDetails userDetails) {
        // ToDo: 비회원일 때 분기처리 주의
        Long userId = getUserIdFromUserDetails(userDetails);
        if (userId == null) {
            throw new UnAuthorizedException(LOGIN_REQUIRED_ERROR_MESSAGE);
        }

        MyUserDTO myUserDTO = userService.getMyUser(userId);
        if (myUserDTO == null) {
            throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
        }

        return myUserDTO;
    }

    @GetMapping("/{userId}")
    @UserControllerDocs.GetOtherUserDocs
    public OtherUserDTO getOtherUser(@PathVariable @Valid @UserValidation.UserId long userId) {
        // ToDo: 비회원일 때 분기처리 주의
        OtherUserDTO otherUserDTO = userService.getOtherUser(userId);
        if (otherUserDTO == null) {
            throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
        }

        return otherUserDTO;
    }

    @PutMapping("")
    @UserControllerDocs.UpdateMyUserDocs
    public UpdateMyUserResponseDTO updateMyUser(@AuthenticationPrincipal UserDetails userDetails,
                                                @RequestBody @Valid UpdateMyUserRequestDTO updateMyUserRequestDTO,
                                                Errors errors)
    {
        NotValidException.throwIfErrorExists(errors);

        Long userId = getUserIdFromUserDetails(userDetails);

        UniqueColumnValidator.validateUniqueUserEmailAddress(uniqueColumnValidator,
                "userEmailAddress",
                updateMyUserRequestDTO.getUserEmailAddress(),
                userId,
                errors);
        UniqueColumnValidator.validateUniqueUserPhoneNumber(uniqueColumnValidator,
                "userPhoneNumber",
                updateMyUserRequestDTO.getUserPhoneNumber(),
                userId,
                errors);
        String userNickname = updateMyUserRequestDTO.getUserNickname();
        if (userNickname != null) {
            UniqueColumnValidator.validateUniqueUserNickname(uniqueColumnValidator,
                    "userNickname",
                    userNickname,
                    userId,
                    errors);
        }

        Integer userGenderCode = updateMyUserRequestDTO.getUserGenderCode();

        if (userGenderCode != null) {
            List<CodeExistValidator.TargetCodeItem> usedCodeList = new LinkedList<>();
            usedCodeList.add(CodeExistValidator.TargetCodeItem.builder()
                    .codeItem(CodeCategoryNameAndCodeName.builder()
                            .codeCategoryName(CodeCategoryName.GENDER.getName())
                            .code(userGenderCode)
                            .build())
                    .field("userGenderCode")
                    .build());

            CodeExistValidator.validateCodeExists(codeExistValidator, usedCodeList, errors);
        }

        NotValidException.throwIfErrorExists(errors);

        /*
        엔티티 db에서 불러온 후 수정 vs update 쿼리 QueryDSL로 만들기
        - 엔티티를 db에서 불러온 후 수정할 경우 select 쿼리를 한 번 호출하게 되지만 유지보수 시 엔티티를 한 곳에서 수정해주면 돼서 편해짐
            -> 서비스 계층에서 엔티티 수정 완료
        - update 쿼리 QueryDSL로 만들 경우 용도에 따라 update 쿼리를 수동으로 여러 개 만들어야 함
            -> 레포지토리 계층까지 영향
         */
        UpdateMyUserResponseDTO updateMyUserResponseDTO = userService.updateMyUser(userId, updateMyUserRequestDTO);
        if (updateMyUserResponseDTO == null) {
            // ToDo: updateMyUser에서 null을 return하는 이유가 변할 수 있으니 서비스 단에서 적절한 Exception을 던지는 것이 나을 듯
            throw new NotFoundException(USER_NOT_FOUND_MESSAGE);
        }

        return updateMyUserResponseDTO;
    }

    @PostMapping("/login/kakao")
    @UserControllerDocs.KakaoLoginDocs
    public TokenResponseDTO kakaoLogin(@RequestBody @Valid KakaoLoginRequestDTO kakaoLoginRequestDTO, Errors errors) {
        NotValidException.throwIfErrorExists(errors);

        KakaoLoginService.KakaoLoginResult kakaoLoginResult =
                kakaoLoginService.loginKakaoUser(kakaoLoginRequestDTO.getAuthorizationCode());

        TokenResponseDTO tokenResponseDTO = kakaoLoginResult.getTokenResponseDTO();
        if (tokenResponseDTO == null) {
            throw new NotFoundException(kakaoLoginResult.getKakaoId().toString());
        }

        return tokenResponseDTO;
    }

    @PostMapping("/login/naver")
    @UserControllerDocs.NaverLoginDocs
    public TokenResponseDTO naverLogin(@RequestBody @Valid NaverLoginRequestDTO naverLoginRequestDTO, Errors errors) {
        NotValidException.throwIfErrorExists(errors);

        return naverLoginService.loginNaverUser(naverLoginRequestDTO.getNaverAccessToken());
    }
}
