package com.junyounggoat.dreamstore.userservice.controller;

import com.junyounggoat.dreamstore.userservice.constant.UniqueColumn;
import com.junyounggoat.dreamstore.userservice.dto.CreateUserRequestDTO;
import com.junyounggoat.dreamstore.userservice.dto.CreateUserResponseDTO;
import com.junyounggoat.dreamstore.userservice.service.UserService;
import com.junyounggoat.dreamstore.userservice.swagger.UserControllerDocs;
import com.junyounggoat.dreamstore.userservice.validation.NotValidException;
import com.junyounggoat.dreamstore.userservice.validation.UniqueColumnValidator;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "사용자 컨트롤러")
public class UserController {
    private final UserService userService;
    private final UniqueColumnValidator uniqueColumnValidator;

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

        // ToDo: 사용자 동의항목 필수값 검증
        // ToDo: 사용자개인정보사용기간코드 검증

        if (errors.hasErrors()) {
            throw NotValidException.builder().errors(errors).build();
        }

        return userService.createUserByLoginCredentials(createUserRequestDTO);
    }

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
}
