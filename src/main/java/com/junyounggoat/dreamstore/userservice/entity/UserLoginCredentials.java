package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Entity
@Builder(toBuilder = true)
@Getter
public class UserLoginCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long userLoginCredentialsId;

    @ManyToOne
    @JoinColumn(name = "userLoginCategoryId", nullable = false)
    private UserLoginCategory userLoginCategory;

    @Column(length = 30)
    @NotBlank
    @Pattern(
            regexp = "^[\\w가-힣\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-+<>@\\#$%&\\\\\\=\\(\\'\\\"]{4,20}$",
            message = "영문, 숫자, 한글, 특수문자로 4~20자를 입력해주세요."
            // ToDo: 메시지 관리 파일 분리 필요
    )
    private String loginUserName;

    @Column(nullable = false, length = 72)
    private String loginUserPassword;

    /* DTO와 엔티티를 분리하고 검증 어노테이션만 따로 정의하고 재사용할걸 그랬나 싶다. */
    @Transient
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*[\\{\\}\\[\\]\\/?.,;:|\\)*~`!^\\-+<>@\\#$%&\\\\\\=\\(\\'\\\"])(?=.*[0-9]).{8,50}$",
            message = "영문, 숫자, 특수문자를 모두 포함해서 8~50자를 입력해주세요."
    )
    private String rawLoginUserPassword;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
