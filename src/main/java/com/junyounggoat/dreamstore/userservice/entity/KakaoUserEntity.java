package com.junyounggoat.dreamstore.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
public class KakaoUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long kakaoUserId;

    @ManyToOne
    @JoinColumn(name = "userLoginCategoryId", nullable = false)
    private UserLoginCategoryEntity userLoginCategory;
}
