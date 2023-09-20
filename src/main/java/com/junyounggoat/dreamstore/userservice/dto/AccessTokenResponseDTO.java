package com.junyounggoat.dreamstore.userservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccessTokenResponseDTO {
    private String accessToken;
}
