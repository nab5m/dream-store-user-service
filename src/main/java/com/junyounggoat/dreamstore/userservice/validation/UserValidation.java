package com.junyounggoat.dreamstore.userservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public abstract class UserValidation {
    public static final long USER_ID_MIN_VALUE = 0L;
    public static final int USER_PERSON_NAME_MIN_LENGTH = 2;
    public static final int USER_PERSON_NAME_MAX_LENGTH = 30;
    public static final String USER_PERSON_NAME_MESSAGE = USER_PERSON_NAME_MIN_LENGTH + "~"
            + USER_PERSON_NAME_MAX_LENGTH + "자의 이름을 입력해주세요.";
    public static final int USER_EMAIL_ADDRESS_MAX_LENGTH = 320;
    public static final String USER_EMAIL_ADDRESS_MESSAGE = USER_EMAIL_ADDRESS_MAX_LENGTH + "자 이하의 이메일 주소를 입력해주세요.";
    public static final int USER_PHONE_NUMBER_MIN_LENGTH = 8;
    public static final int USER_PHONE_NUMBER_MAX_LENGTH = 15;
    public static final String USER_PHONE_NUMBER_MESSAGE = USER_PHONE_NUMBER_MIN_LENGTH + "~" + USER_PHONE_NUMBER_MAX_LENGTH + "자의 휴대폰 번호를 입력해주세요.";
    public static final int USER_NICKNAME_MIN_LENGTH = 1;
    public static final int USER_NICKNAME_MAX_LENGTH = 30;

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Min(value = USER_ID_MIN_VALUE)
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation
    public @interface UserId {
        String message() default "사용자 ID가 0 보다 작습니다.";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Size(min = USER_PERSON_NAME_MIN_LENGTH, max = USER_PERSON_NAME_MAX_LENGTH)
    @NotBlank
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation
    public @interface UserPersonName {
        String message() default USER_PERSON_NAME_MESSAGE;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Size(max = USER_EMAIL_ADDRESS_MAX_LENGTH)
    @NotBlank
    @Pattern(regexp = "^[\\w._%+-]+@[\\w._-]+\\.[\\w]{2,}$")
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation
    public @interface UserEmailAddress {
        String message() default USER_EMAIL_ADDRESS_MESSAGE;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @NotBlank
    @Pattern(regexp = "^\\d{" + USER_PHONE_NUMBER_MIN_LENGTH +  "," + USER_PHONE_NUMBER_MAX_LENGTH + "}$")
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation
    public @interface UserPhoneNumber {
        String message() default USER_PHONE_NUMBER_MESSAGE;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Size(min = USER_NICKNAME_MIN_LENGTH, max = USER_NICKNAME_MAX_LENGTH)
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation
    public @interface UserNickname {
        String message() default USER_NICKNAME_MIN_LENGTH + "~" + USER_NICKNAME_MAX_LENGTH + "자의 닉네임을 입력해주세요.";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
}
