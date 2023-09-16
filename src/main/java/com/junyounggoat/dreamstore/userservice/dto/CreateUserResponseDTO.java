package com.junyounggoat.dreamstore.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateUserResponseDTO {
    private String accessToken;
}
