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

public abstract class UserLoginCredentialsValidation {
    public static final long USER_LOGIN_CREDENTIALS_ID_MIN_VALUE = 0;
    public static final int LOGIN_USER_NAME_MIN_LENGTH = 4;
    public static final int LOGIN_USER_NAME_MAX_LENGTH = 20;
    public static final String LOGIN_USER_NAME_MESSAGE = "영문, 숫자, 한글, 특수문자로 " + LOGIN_USER_NAME_MIN_LENGTH + "~" + LOGIN_USER_NAME_MAX_LENGTH + "자를 입력해주세요.";
    public static final int LOGIN_USER_PASSWORD_MAX_LENGTH = 72;
    public static final int RAW_LOGIN_USER_PASSWORD_MIN_LENGTH = 8;
    public static final int RAW_LOGIN_USER_PASSWORD_MAX_LENGTH = 50;
    public static final String RAW_LOGIN_USER_PASSWORD_MESSAGE = "영문, 숫자, 특수문자를 모두 포함해서 " + RAW_LOGIN_USER_PASSWORD_MIN_LENGTH + "~"
            + RAW_LOGIN_USER_PASSWORD_MAX_LENGTH + "자의 비밀번호를 입력해주세요.";

    @Target({ FIELD })
    @Retention(RUNTIME)
    @Min(value = USER_LOGIN_CREDENTIALS_ID_MIN_VALUE)
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation
    public @interface UserLoginCredentialsId {
        String message() default "사용자로그인자격증명 ID가 0 보다 작습니다.";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Pattern(
            regexp = "^[\\w가-힣\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-+<>@\\#$%&\\\\\\=\\(\\'\\\"]{"
                    + LOGIN_USER_NAME_MIN_LENGTH + "," + LOGIN_USER_NAME_MAX_LENGTH + "}$"
    )
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation
    public @interface LoginUserName {
        // ToDo: 메시지 관리 파일 분리 필요
        String message() default LOGIN_USER_NAME_MESSAGE;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @LoginUserName
    @NotBlank
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation
    public @interface LoginUserNameNotBlank {
        // ToDo: 메시지 관리 파일 분리 필요
        String message() default LOGIN_USER_NAME_MESSAGE;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
    @Retention(RUNTIME)
    @Size(max = LOGIN_USER_PASSWORD_MAX_LENGTH)
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation
    public @interface LoginUserPassword {
        String message() default LOGIN_USER_PASSWORD_MAX_LENGTH + "자 이하의 비밀번호를 입력해주세요.";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Target({ FIELD })
    @Retention(RUNTIME)
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-+<>@\\#$%&\\\\\\=\\(\\'\\\"])(?=.*[0-9]).{"
                + RAW_LOGIN_USER_PASSWORD_MIN_LENGTH + "," + RAW_LOGIN_USER_PASSWORD_MAX_LENGTH + "}$"
    )
    @Constraint(validatedBy = {})
    @ReportAsSingleViolation
    public @interface RawLoginUserPassword {
        String message() default RAW_LOGIN_USER_PASSWORD_MESSAGE;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }
}
