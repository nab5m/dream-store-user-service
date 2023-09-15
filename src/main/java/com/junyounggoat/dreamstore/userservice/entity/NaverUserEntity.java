package com.junyounggoat.dreamstore.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
public class NaverUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long naverUserId;

    @ManyToOne
    @JoinColumn(name = "userLoginCategoryId", nullable = false)
    private UserLoginCategoryEntity userLoginCategory;
}
