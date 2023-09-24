package com.junyounggoat.dreamstore.user.api.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
