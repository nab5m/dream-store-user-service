package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserAgreementItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userAgreementItemId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    private int userAgreementItemCode;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
