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

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;

    private LocalDateTime getUsageEndDateTime(int userPrivacyUsagePeriodCode) {
        if (userPrivacyUsagePeriodCode == UserPrivacyUsagePeriodCode.FOREVER.getCode()) {
            return DateTimeConstants.DB_MAX_DATETIME;
        } else if (userPrivacyUsagePeriodCode >= DateTimeConstants.ONE_YEAR_SECONDS) {
            return this.usageStartDateTime.plusYears(userPrivacyUsagePeriodCode / DateTimeConstants.ONE_YEAR_SECONDS);
        } else if (userPrivacyUsagePeriodCode >= DateTimeConstants.ONE_MONTH_SECONDS) {
            return this.usageStartDateTime.plusMonths(userPrivacyUsagePeriodCode / DateTimeConstants.ONE_MONTH_SECONDS);
        } else {
            return this.usageStartDateTime.plusSeconds(userPrivacyUsagePeriodCode / DateTimeConstants.ONE_MONTH_SECONDS);
        }
    }
}
