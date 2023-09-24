package com.junyounggoat.dreamstore.user.api.dto;

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
