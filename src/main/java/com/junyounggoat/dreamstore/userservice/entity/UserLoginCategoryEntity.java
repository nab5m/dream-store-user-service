package com.junyounggoat.dreamstore.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;


@Entity
public class UserLoginCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private Long userLoginCategoryId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "userLoginCategoryCodeId", nullable = false)
    private CodeEntity userLoginCategory;

    private CreationDateTime creationDateTime;

    private DeletionDateTime deletionDateTime;
}
