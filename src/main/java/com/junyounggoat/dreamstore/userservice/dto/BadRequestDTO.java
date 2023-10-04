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

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof BadRequestDTO)) {
            return false;
        }
        BadRequestDTO target = (BadRequestDTO) obj;

        return fieldErrors.equals(target.getFieldErrors()) &&
                notFieldErrors.equals(target.getNotFieldErrors());
    }
}
