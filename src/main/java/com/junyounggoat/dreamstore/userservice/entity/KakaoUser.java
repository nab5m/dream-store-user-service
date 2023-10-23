package com.junyounggoat.dreamstore.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class KakaoUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long kakaoUserId;

    @ManyToOne
    @JoinColumn(name = "kakaoUserLoginCategoryId", nullable = false)
    private UserLoginCategory userLoginCategory;

    @Column(nullable = false)
    private Long kakaoId;

    private LocalDateTime kakaoUserConnectionDateTime;

    @Size(max = 200)
    private String kakaoUserNickname;

    @Size(max = 512)
    private String kakaoUserThumbnailImageUrl;

    @Size(max = 512)
    private String kakaoUserProfileImageUrl;

    private Boolean kakaoUserDefaultImageFlag;

    @Embedded
    private TimestampEmbeddable timestamp;
}
