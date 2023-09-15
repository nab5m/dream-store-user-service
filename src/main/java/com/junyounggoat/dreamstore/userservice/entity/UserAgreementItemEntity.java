package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class UserAgreementItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userAgreementItemId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private int userAgreementItemCode;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
