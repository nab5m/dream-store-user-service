package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserAgreementItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAgreementItemId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "userAgreementItemCodeId", nullable = false)
    private CodeEntity userAgreementItem;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
