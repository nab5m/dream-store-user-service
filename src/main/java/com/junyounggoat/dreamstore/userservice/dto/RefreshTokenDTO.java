package com.junyounggoat.dreamstore.userservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefreshTokenDTO {
    @NotNull
    private String refreshToken;
}
