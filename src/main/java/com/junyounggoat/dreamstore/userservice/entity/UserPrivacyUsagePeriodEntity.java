package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class UserPrivacyUsagePeriodEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private Long userPrivacyUsagePeriodId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime usageStartDateTime;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime usageEndDateTime;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
