package com.junyounggoat.dreamstore.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KakaoLoginRequestDTO {
    @NotBlank
    private String authorizationCode;
}
