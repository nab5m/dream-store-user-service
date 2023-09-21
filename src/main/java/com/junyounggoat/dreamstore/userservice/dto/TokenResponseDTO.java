package com.junyounggoat.dreamstore.userservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
