package com.junyounggoat.dreamstore.userservice.entity;

import static com.junyounggoat.dreamstore.userservice.validation.UserLoginCredentialsValidation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserLoginCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UserLoginCredentialsId
    private long userLoginCredentialsId;

    @ManyToOne
    @JoinColumn(name = "userLoginCategoryId", nullable = false)
    private UserLoginCategory userLoginCategory;

    @Column(length = LOGIN_USER_NAME_MAX_LENGTH)
    @LoginUserName
    private String loginUserName;

    @Column(length = LOGIN_USER_PASSWORD_MAX_LENGTH)
    @LoginUserPassword
    private String loginUserPassword;

    @Embedded
    private TimestampEmbeddable timestamp;
}
