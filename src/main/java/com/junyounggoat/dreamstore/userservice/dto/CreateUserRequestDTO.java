package com.junyounggoat.dreamstore.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.junyounggoat.dreamstore.userservice.constant.UserLoginCategoryCode;
import com.junyounggoat.dreamstore.userservice.entity.User;
import com.junyounggoat.dreamstore.userservice.entity.UserLoginCredentials;
import com.junyounggoat.dreamstore.userservice.validation.UserLoginCredentialsValidation;
import com.junyounggoat.dreamstore.userservice.validation.UserValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

// ToDo: 문서화 어노테이션 매핑을 DTO에 적용하는 건 어떨까?
@Getter
public class CreateUserRequestDTO {
    private final int userLoginCategoryCode = UserLoginCategoryCode.userLoginCredentials.getCode();

    @Valid
    @NotNull
    private UserDTO user;

    @Valid
    @NotNull
    private UserLoginCredentialsDTO userLoginCredentials;

    @NotEmpty
    private List<Integer> userAgreementItemCodeList;

    @NotNull
    private Integer userPrivacyUsagePeriodCode;

    private KakaoUserDTO kakaoUser;

    @Builder
    @Getter
    public static class UserDTO {
        @UserValidation.UserPersonNameNotBlank
        private String userPersonName;

        @UserValidation.UserEmailAddressNotBlank
        private String userEmailAddress;

        @UserValidation.UserPhoneNumberNotBlank
        private String userPhoneNumber;

        public User.UserBuilder toUserBuilder() {
            return User.builder()
                    .userPersonName(userPersonName)
                    .userEmailAddress(userEmailAddress)
                    .userPhoneNumber(userPhoneNumber);
        }
    }

    @Builder
    @Getter
    public static class UserLoginCredentialsDTO {
        @UserLoginCredentialsValidation.LoginUserNameNotBlank
        private String loginUserName;

        @UserLoginCredentialsValidation.RawLoginUserPassword
        private String rawLoginUserPassword;

        public UserLoginCredentials.UserLoginCredentialsBuilder toUserLoginCredentialsBuilder(PasswordEncoder passwordEncoder) {
            return UserLoginCredentials.builder()
                    .loginUserName(loginUserName)
                    .loginUserPassword(passwordEncoder.encode(rawLoginUserPassword));
        }
    }

    @Builder
    @Getter
    public static class KakaoUserDTO {
        private long kakaoId;
    }
}
