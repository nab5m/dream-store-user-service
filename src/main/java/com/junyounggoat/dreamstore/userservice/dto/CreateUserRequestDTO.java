package com.junyounggoat.dreamstore.userservice.dto;

import com.junyounggoat.dreamstore.userservice.entity.UserLoginCredentials;
import com.junyounggoat.dreamstore.userservice.validation.UserLoginCredentialsValidation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

// ToDo: 문서화 어노테이션 매핑을 DTO에 적용하는 건 어떨까?
@Builder
@Getter
public class CreateUserRequestDTO implements NewMemberCommonFields {
    @Valid
    @NotNull
    private UserLoginCredentialsDTO userLoginCredentials;

    @Valid
    @NotNull
    private CreateUserDTO user;

    @NotEmpty
    private List<Integer> userAgreementItemCodeList;

    @NotNull
    private Integer userPrivacyUsagePeriodCode;

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
}
