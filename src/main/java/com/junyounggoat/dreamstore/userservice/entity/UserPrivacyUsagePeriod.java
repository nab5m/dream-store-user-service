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

import static com.junyounggoat.dreamstore.userservice.constant.DateTimeConstants.*;

@Entity
@NoArgsConstructor
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

    @Column(nullable = false)
    @NotNull
    private LocalDateTime usageStartDateTime;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime usageEndDateTime;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;

    @Builder
    public UserPrivacyUsagePeriod(User user, int userPrivacyUsagePeriodCode) {
        this.user = user;
        this.userPrivacyUsagePeriodCode = userPrivacyUsagePeriodCode;
        // ToDo: 타임존 확인 필요
        this.usageStartDateTime = LocalDateTime.now();
        this.setUsageEndDateTime(userPrivacyUsagePeriodCode);
    }

    private void setUsageEndDateTime(int userPrivacyUsagePeriodCode) {
        if (userPrivacyUsagePeriodCode == UserPrivacyUsagePeriodCode.FOREVER.getCode()) {
            this.usageEndDateTime = DB_MAX_DATETIME;
        } else if (userPrivacyUsagePeriodCode >= ONE_YEAR_SECONDS) {
            this.usageEndDateTime = this.usageStartDateTime.plusYears(userPrivacyUsagePeriodCode / ONE_YEAR_SECONDS);
        } else if (userPrivacyUsagePeriodCode >= ONE_MONTH_SECONDS) {
            this.usageEndDateTime = this.usageStartDateTime.plusMonths(userPrivacyUsagePeriodCode / ONE_MONTH_SECONDS);
        } else {
            this.usageEndDateTime = this.usageStartDateTime.plusSeconds(userPrivacyUsagePeriodCode / ONE_MONTH_SECONDS);
        }
    }
}
