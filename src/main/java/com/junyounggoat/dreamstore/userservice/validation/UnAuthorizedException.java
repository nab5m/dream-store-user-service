package com.junyounggoat.dreamstore.userservice.validation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class UnAuthorizedException extends RuntimeException {
    public static final String LOGIN_REQUIRED_ERROR_MESSAGE = "로그인이 필요합니다.";

    public UnAuthorizedException(String message) {
        super(message);
    }
}
