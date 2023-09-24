package com.junyounggoat.dreamstore.user.api.validation;


import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.Errors;

@Builder
@Getter
public class NotValidException extends RuntimeException {
    private Errors errors;

    public static void throwIfErrorExists(Errors errors) {
        if (errors.hasErrors()) {
            throw NotValidException.builder().errors(errors).build();
        }
    }
}
