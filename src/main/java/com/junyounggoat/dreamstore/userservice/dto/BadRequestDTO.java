package com.junyounggoat.dreamstore.userservice.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@EqualsAndHashCode
public class BadRequestDTO {
    private Map<String, String> fieldErrors;
    private List<String> notFieldErrors;
}
