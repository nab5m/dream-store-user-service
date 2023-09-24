package com.junyounggoat.dreamstore.user.api.dto;

import com.junyounggoat.dreamstore.user.repository.entity.User;
import com.junyounggoat.dreamstore.user.repository.entity.UserLoginCredentials;
import com.junyounggoat.dreamstore.user.repository.validation.UserLoginCredentialsValidation;
import com.junyounggoat.dreamstore.user.repository.validation.UserValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

// ToDo: 문서화 어노테이션 매핑을 DTO에 적용하는 건 어떨까?
@Builder
@Getter
public class CreateUserRequestDTO {
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

    @Builder
    @Getter
    public static class UserDTO {
        @UserValidation.UserPersonName
        private String userPersonName;

        @UserValidation.UserEmailAddress
        private String userEmailAddress;

        @UserValidation.UserPhoneNumber
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
        @UserLoginCredentialsValidation.LoginUserName
        private String loginUserName;

        @UserLoginCredentialsValidation.RawLoginUserPassword
        private String rawLoginUserPassword;

        public UserLoginCredentials.UserLoginCredentialsBuilder toUserLoginCredentialsBuilder(PasswordEncoder passwordEncoder) {
            return UserLoginCredentials.builder()
                    .loginUserName(loginUserName)
                    .loginUserPassword(passwordEncoder.encode(rawLoginUserPassword));
        }
    }
}
