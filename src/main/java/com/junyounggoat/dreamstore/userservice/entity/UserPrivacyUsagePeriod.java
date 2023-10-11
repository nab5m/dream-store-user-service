package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.junyounggoat.dreamstore.userservice.constant.UserPrivacyUsagePeriodCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import com.junyounggoat.dreamstore.userservice.constant.DateTimeConstants;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserPrivacyUsagePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long userPrivacyUsagePeriodId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    @NotNull
    private int userPrivacyUsagePeriodCode;

    // ToDo: Timezone 확인필요
    @Column(nullable = false)
    @NotNull
    private LocalDateTime usageStartDateTime;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime usageEndDateTime;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;

    public UserPrivacyUsagePeriod withUsageEndDateTime() {
        LocalDateTime usageEndDateTime = null;

        if (userPrivacyUsagePeriodCode == UserPrivacyUsagePeriodCode.FOREVER.getCode()) {
            usageEndDateTime = DateTimeConstants.DB_MAX_DATETIME;
        } else if (userPrivacyUsagePeriodCode == UserPrivacyUsagePeriodCode.ONE_YEAR.getCode()) {
            usageEndDateTime = this.usageStartDateTime.plusYears(1);
        } else if (userPrivacyUsagePeriodCode >= UserPrivacyUsagePeriodCode.THREE_YEAR.getCode()) {
            usageEndDateTime = this.usageStartDateTime.plusYears(3);
        } else if (userPrivacyUsagePeriodCode >= UserPrivacyUsagePeriodCode.FIVE_YEAR.getCode()) {
            usageEndDateTime = this.usageStartDateTime.plusYears(5);
        } else {
            usageEndDateTime = this.usageStartDateTime.plusSeconds(userPrivacyUsagePeriodCode);
        }

        return this.toBuilder()
                .usageEndDateTime(usageEndDateTime)
                .build();
    }
}
