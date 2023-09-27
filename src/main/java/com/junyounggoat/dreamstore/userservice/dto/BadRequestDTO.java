package com.junyounggoat.dreamstore.userservice.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class BadRequestDTO {
    private Map<String, String> fieldErrors;
    private List<String> notFieldErrors;
}
