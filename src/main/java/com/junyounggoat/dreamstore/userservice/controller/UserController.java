package com.junyounggoat.dreamstore.userservice.controller;

import com.junyounggoat.dreamstore.userservice.constant.CodeCategoryName;
import com.junyounggoat.dreamstore.userservice.constant.UniqueColumn;
import com.junyounggoat.dreamstore.userservice.dto.*;
import com.junyounggoat.dreamstore.userservice.repository.CodeRepository.CodeCategoryNameAndCodeName;
import com.junyounggoat.dreamstore.userservice.service.UserService;
import com.junyounggoat.dreamstore.userservice.swagger.UserControllerDocs;
import com.junyounggoat.dreamstore.userservice.util.JwtUtil;
import com.junyounggoat.dreamstore.userservice.validation.CodeExistValidator;
import com.junyounggoat.dreamstore.userservice.validation.NotValidException;
import com.junyounggoat.dreamstore.userservice.validation.RequiredUserAgreementItemValidator;
import com.junyounggoat.dreamstore.userservice.validation.UniqueColumnValidator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.RequiredTypeException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

import static com.junyounggoat.dreamstore.userservice.config.OpenApiConfig.SECURITY_SCHEME_NAME;
import static com.junyounggoat.dreamstore.userservice.util.JwtUtil.JWT_CLAIM_USER_ID;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "사용자 컨트롤러")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final UniqueColumnValidator uniqueColumnValidator;
    private final RequiredUserAgreementItemValidator requiredUserAgreementItemValidator;
    private final CodeExistValidator codeExistValidator;

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    @UserControllerDocs.CreateUserDocs
    public AccessTokenResponseDTO createUser(@RequestBody @Valid CreateUserRequestDTO createUserRequestDTO, Errors errors) {
        if (errors.hasErrors()) {
            throw NotValidException.builder().errors(errors).build();
        }

        validateUniqueUserEmailAddress("user.userEmailAddress", createUserRequestDTO.getUser().getUserEmailAddress(), errors);
        validateUniqueUserPhoneNumber("user.userPhoneNumber", createUserRequestDTO.getUser().getUserPhoneNumber(), errors);
        validateUniqueLoginUserName("userLoginCredentials.loginUserName", createUserRequestDTO.getUserLoginCredentials().getLoginUserName(), errors);

        // 이렇게 검증이 필요하다면 그냥 fk를 걸었지 싶다
        List<CodeExistValidator.TargetCodeItem> usedCodeList = new LinkedList<>();
        createUserRequestDTO.getUserAgreementItemCodeList().forEach((userAgreementItemCode) -> {
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
                        .code(createUserRequestDTO.getUserPrivacyUsagePeriodCode())
                        .build())
                .field("userPrivacyUsagePeriodCode")
                .build());

        validateCodeExists(usedCodeList, errors);
        validateRequiredUserAgreementItem("userAgreementItemCodeList", createUserRequestDTO.getUserAgreementItemCodeList(), errors);

        if (errors.hasErrors()) {
            throw NotValidException.builder().errors(errors).build();
        }

        return userService.createUserByLoginCredentials(createUserRequestDTO);
    }

    // ToDo: 이것도 Validator 클래스 안 쪽으로 집어넣을까?
    private void validateUniqueUserEmailAddress(String field, String userEmailAddress, Errors errors) {
        uniqueColumnValidator.validate(
                UniqueColumnValidator.Target.builder()
                        .field(field)
                        .uniqueColumn(UniqueColumn.UserEmailAddress)
                        .value(userEmailAddress)
                        .build(),
                errors
        );
    }

    private void validateUniqueUserPhoneNumber(String field, String userPhoneNumber, Errors errors) {
        uniqueColumnValidator.validate(
                UniqueColumnValidator.Target.builder()
                        .field(field)
                        .uniqueColumn(UniqueColumn.UserPhoneNumber)
                        .value(userPhoneNumber)
                        .build(),
                errors
        );
    }

    private void validateUniqueLoginUserName(String field, String loginUserName, Errors errors) {
        uniqueColumnValidator.validate(
                UniqueColumnValidator.Target.builder()
                        .field(field)
                        .uniqueColumn(UniqueColumn.LoginUserName)
                        .value(loginUserName)
                        .build(),
                errors
        );
    }

    private void validateRequiredUserAgreementItem(String field, List<Integer> codeList, Errors errors) {
        requiredUserAgreementItemValidator.validate(
                RequiredUserAgreementItemValidator.Target.builder()
                        .field(field)
                        .targetCodeList(codeList)
                        .build(),
                errors
        );
    }

    private void validateCodeExists(List<CodeExistValidator.TargetCodeItem> codeItemList, Errors errors) {
        codeExistValidator.validate(
                CodeExistValidator.Target.builder()
                        .targetCodeList(codeItemList)
                        .build(),
                errors
        );
    }


    @PostMapping("/login")
    @UserControllerDocs.LoginDocs
    public AccessTokenResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO, Errors errors) {
        if (errors.hasErrors()) {
            throw NotValidException.builder().errors(errors).build();
        }

        AccessTokenResponseDTO response = userService.login(loginRequestDTO.getLoginUserName(), loginRequestDTO.getRawLoginUserPassword());

        if (response == null) {
            errors.reject("IncorrectLoginUserNameOrLoginUserPassword", "아이디나 비밀번호가 일치하지 않습니다.");
            throw NotValidException.builder().errors(errors).build();
        }

        return response;
    }

    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public static class UnAuthorizedException extends RuntimeException {
        public UnAuthorizedException(String message) {
            super(message);
        }
    }

    @GetMapping("/mine")
    @UserControllerDocs.GetMyUserProfileDocs
    public MyUserProfileDTO getMyUserProfile(@AuthenticationPrincipal UserDetails userDetails) {
        // ToDo: 예외처리를 더 깔끔하게 할 수 없을까?
        String ERROR_MESSAGE = "로그인이 필요합니다.";

        if (userDetails == null) {
            throw new UnAuthorizedException(ERROR_MESSAGE);
        }

        String token = userDetails.getPassword();
        Claims claims = JwtUtil.getClaims(token);
        if (claims == null) {
            throw new UnAuthorizedException(ERROR_MESSAGE);
        }

        Long userId;
        try {
            userId = claims.get(JWT_CLAIM_USER_ID, Long.class);
        } catch (RequiredTypeException e) {
            logger.info("TypeCastUserId Failed : " + token);

            throw new UnAuthorizedException(ERROR_MESSAGE);
        }

        if (userId == null) {
            throw new UnAuthorizedException(ERROR_MESSAGE);
        }

        MyUserProfileDTO myUserProfileDTO = userService.getMyUserProfile(userId);
        if (myUserProfileDTO == null) {
            throw new UnAuthorizedException("존재하지 않는 사용자입니다.");
        }

        return myUserProfileDTO;
    }
}
