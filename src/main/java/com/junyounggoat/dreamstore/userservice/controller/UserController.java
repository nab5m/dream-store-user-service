package com.junyounggoat.dreamstore.userservice.controller;

import com.junyounggoat.dreamstore.userservice.constant.CodeCategoryName;
import com.junyounggoat.dreamstore.userservice.constant.UniqueColumn;
import com.junyounggoat.dreamstore.userservice.dto.CreateUserRequestDTO;
import com.junyounggoat.dreamstore.userservice.dto.CreateUserResponseDTO;
import com.junyounggoat.dreamstore.userservice.repository.CodeRepository.CodeCategoryNameAndCodeName;
import com.junyounggoat.dreamstore.userservice.service.UserService;
import com.junyounggoat.dreamstore.userservice.swagger.UserControllerDocs;
import com.junyounggoat.dreamstore.userservice.validation.CodeExistValidator;
import com.junyounggoat.dreamstore.userservice.validation.NotValidException;
import com.junyounggoat.dreamstore.userservice.validation.RequiredUserAgreementItemValidator;
import com.junyounggoat.dreamstore.userservice.validation.UniqueColumnValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "사용자 컨트롤러")
public class UserController {
    private final UserService userService;
    private final UniqueColumnValidator uniqueColumnValidator;
    private final RequiredUserAgreementItemValidator requiredUserAgreementItemValidator;
    private final CodeExistValidator codeExistValidator;

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    @UserControllerDocs.CreateUserDocs
    public CreateUserResponseDTO createUser(@RequestBody @Valid CreateUserRequestDTO createUserRequestDTO, Errors errors) {
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
}
