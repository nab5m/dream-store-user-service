package com.junyounggoat.dreamstore.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LoginRequestDTO {
    @NotNull
    private String loginUserName;

    @NotNull
    private String rawLoginUserPassword;
}
