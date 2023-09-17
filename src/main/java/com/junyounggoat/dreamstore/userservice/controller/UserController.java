package com.junyounggoat.dreamstore.userservice.controller;

import com.junyounggoat.dreamstore.userservice.dto.CreateUserRequestDTO;
import com.junyounggoat.dreamstore.userservice.dto.CreateUserResponseDTO;
import com.junyounggoat.dreamstore.userservice.service.UserService;
import com.junyounggoat.dreamstore.userservice.swagger.UserControllerDocs;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "사용자 컨트롤러")
public class UserController {
    private final UserService userService;

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.CREATED)
    @UserControllerDocs.CreateUserDocs
    public CreateUserResponseDTO createUser(@RequestBody @Valid CreateUserRequestDTO createUserRequestDTO) {
        // ToDo: 중복 검증, 사용자 동의항목 필수값 검증
        return userService.createUserByLoginCredentials(createUserRequestDTO);
    }
}
