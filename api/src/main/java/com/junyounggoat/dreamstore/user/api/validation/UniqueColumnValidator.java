package com.junyounggoat.dreamstore.user.api.validation;

import com.junyounggoat.dreamstore.user.repository.entity.User;
import com.junyounggoat.dreamstore.user.repository.entity.UserLoginCredentials;
import com.junyounggoat.dreamstore.user.repository.UserRepository;
import com.junyounggoat.dreamstore.user.api.constant.UniqueColumn;
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
    private static final String ERROR_CODE = "Duplication";

    private final UserRepository userRepository;

    @Builder
    @Getter
    public static class Target<T> {
        private Long excludingRowId;
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
            case UserEmailAddress -> validateUniqueUserEmailAddress(target, errors);
            case UserPhoneNumber -> validateUniqueUserPhoneNumber(target, errors);
            case LoginUserName -> validateUniqueLoginUserName(target, errors);
            default -> throw new RuntimeException("UniqueColumnValidator - Not Supported Column.");
        }
    }

    private void validateUniqueUserEmailAddress(Target target, Errors errors) {
        User user = userRepository.findUserByUserEmailAddress((String) target.getValue(), target.getExcludingRowId());
        if (user != null) {
            String ERROR_MESSAGE = "이미 사용 중인 이메일 주소입니다.";
            try {
                errors.rejectValue(target.getField(), ERROR_CODE, ERROR_MESSAGE);
            } catch (NotReadablePropertyException exception) {
                errors.reject(ERROR_CODE, ERROR_MESSAGE);
            }
        }
    }

    private void validateUniqueUserPhoneNumber(Target target, Errors errors) {
        User user = userRepository.findUserByUserPhoneNumber((String) target.getValue(), target.getExcludingRowId());
        if (user != null) {
            String ERROR_MESSAGE = "이미 사용 중인 휴대폰 번호입니다.";
            try {
                errors.rejectValue(target.getField(), ERROR_CODE, ERROR_MESSAGE);
            } catch (NotReadablePropertyException exception) {
                errors.reject(ERROR_CODE, ERROR_MESSAGE);
            }
        }
    }

    private void validateUniqueLoginUserName(Target target, Errors errors) {
        UserLoginCredentials userLoginCredentials = userRepository.findUserLoginCredentialsByLoginUserName((String) target.getValue());
        if (userLoginCredentials != null) {
            String ERROR_MESSAGE = "이미 사용 중인 아이디입니다.";
            try {
                errors.rejectValue(target.getField(), ERROR_CODE, ERROR_MESSAGE);
            } catch (NotReadablePropertyException exception) {
                errors.reject(ERROR_CODE, ERROR_MESSAGE);
            }
        }
    }

    public static void validateUniqueUserEmailAddress(UniqueColumnValidator uniqueColumnValidator, String field, String userEmailAddress, Errors errors) {
        validateUniqueUserEmailAddress(uniqueColumnValidator, field, userEmailAddress, null, errors);
    }

    public static void validateUniqueUserEmailAddress(UniqueColumnValidator uniqueColumnValidator, String field,
                                               String userEmailAddress, Long excludingRowId, Errors errors) {
        uniqueColumnValidator.validate(
                UniqueColumnValidator.Target.builder()
                        .field(field)
                        .uniqueColumn(UniqueColumn.UserEmailAddress)
                        .excludingRowId(excludingRowId)
                        .value(userEmailAddress)
                        .build(),
                errors
        );
    }

    public static void validateUniqueUserPhoneNumber(UniqueColumnValidator uniqueColumnValidator, String field,
                                                     String userPhoneNumber, Errors errors) {
        validateUniqueUserPhoneNumber(uniqueColumnValidator, field, userPhoneNumber, null, errors);
    }

    public static void validateUniqueUserPhoneNumber(UniqueColumnValidator uniqueColumnValidator, String field,
                                                     String userPhoneNumber, Long excludingRowId, Errors errors) {
        uniqueColumnValidator.validate(
                UniqueColumnValidator.Target.builder()
                        .field(field)
                        .uniqueColumn(UniqueColumn.UserPhoneNumber)
                        .excludingRowId(excludingRowId)
                        .value(userPhoneNumber)
                        .build(),
                errors
        );
    }

    public static void validateUniqueLoginUserName(UniqueColumnValidator uniqueColumnValidator, String field,
                                                   String loginUserName, Errors errors) {
        uniqueColumnValidator.validate(
                UniqueColumnValidator.Target.builder()
                        .field(field)
                        .uniqueColumn(UniqueColumn.LoginUserName)
                        .value(loginUserName)
                        .build(),
                errors
        );
    }
}
