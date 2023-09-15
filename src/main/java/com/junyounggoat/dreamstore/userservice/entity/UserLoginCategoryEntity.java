package com.junyounggoat.dreamstore.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


@Entity
public class UserLoginCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long userLoginCategoryId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private int userLoginCategoryCode;

    private CreationDateTime creationDateTime;

    private DeletionDateTime deletionDateTime;
}
