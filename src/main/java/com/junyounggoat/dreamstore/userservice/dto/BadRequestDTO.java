package com.junyounggoat.dreamstore.userservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class BadRequestDTO {
    private Map<String, String> errors;
}
