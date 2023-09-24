package com.junyounggoat.dreamstore.user.api.dto;

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
