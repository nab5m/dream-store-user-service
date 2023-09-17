package com.junyounggoat.dreamstore.userservice.validation;

import com.junyounggoat.dreamstore.userservice.constant.UniqueColumn;
import com.junyounggoat.dreamstore.userservice.entity.User;
import com.junyounggoat.dreamstore.userservice.entity.UserLoginCredentials;
import com.junyounggoat.dreamstore.userservice.repository.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UniqueColumnValidator implements Validator {
    private final UserRepository userRepository;

    @Builder
    @Getter
    public static class Target<T> {
        private UniqueColumn uniqueColumn;
        private String field;
        private T value;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Target.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object targetObject, Errors errors) {
        Target target = (Target) targetObject;

        switch (target.getUniqueColumn()) {
            case UserEmailAddress -> validateUniqueUserEmailAddress(target.getField(), (String) target.getValue(), errors);
            case UserPhoneNumber -> validateUniqueUserPhoneNumber(target.getField(), (String) target.getValue(), errors);
            case LoginUserName -> validateUniqueLoginUserName(target.getField(), (String) target.getValue(), errors);
            default -> throw new RuntimeException("UniqueColumnValidator - Not Supported Column.");
        }
    }

    private void validateUniqueUserEmailAddress(String field, String userEmailAddress, Errors errors) {
        User user = userRepository.findUserByUserEmailAddress(userEmailAddress);
        if (user != null) {
            try {
                errors.rejectValue(field, "duplication", "이미 사용 중인 이메일 주소입니다.");
            } catch (NotReadablePropertyException exception) {
                errors.reject("duplication", "이미 사용 중인 이메일 주소입니다.");
            }
        }
    }

    private void validateUniqueUserPhoneNumber(String field, String userPhoneNumber, Errors errors) {
        User user = userRepository.findUserByUserPhoneNumber(userPhoneNumber);
        if (user != null) {
            try {
                errors.rejectValue(field, "duplication", "이미 사용 중인 휴대폰 번호입니다.");
            } catch (NotReadablePropertyException exception) {
                errors.reject("duplication", "이미 사용 중인 휴대폰 번호입니다.");
            }
        }
    }

    private void validateUniqueLoginUserName(String field, String loginUserName, Errors errors) {
        UserLoginCredentials userLoginCredentials = userRepository.findUserLoginCredentialsByLoginUserName(loginUserName);
        if (userLoginCredentials != null) {
            try {
                errors.rejectValue(field, "duplication", "이미 사용 중인 아이디입니다.");
            } catch (NotReadablePropertyException exception) {
                errors.reject("duplication", "이미 사용 중인 아이디입니다.");
            }
        }
    }
}
