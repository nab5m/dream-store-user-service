package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Entity
public class UserLoginCredentialsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long userLoginCredentialsId;

    @ManyToOne
    @JoinColumn(name = "userLoginCategoryId", nullable = false)
    private UserLoginCategoryEntity userLoginCategory;

    @Column(length = 30)
    @Pattern(
            regexp = "^[\\w가-힣\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-+<>@\\#$%&\\\\\\=\\(\\'\\\"]{4,20}$",
            message = "영문, 숫자, 한글, 특수문자로 4~20자를 입력해주세요."
            // ToDo: 메시지 관리 파일 분리 필요
    )
    private String loginUserName;

    @Column(nullable = false, length = 72)
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-+<>@\\#$%&\\\\\\=\\(\\'\\\"])(?=.*[0-9]).{8,50}$",
            message = "영문, 숫자, 특수문자를 모두 포함해서 8~50자를 입력해주세요."
    )
    private String loginUserPassword;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
