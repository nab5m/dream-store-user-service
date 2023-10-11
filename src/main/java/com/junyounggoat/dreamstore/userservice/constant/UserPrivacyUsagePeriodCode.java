package com.junyounggoat.dreamstore.userservice.constant;

import lombok.Getter;

@Getter
public enum UserPrivacyUsagePeriodCode {
    FOREVER(0),
    ONE_YEAR(31536000),
    THREE_YEAR(94608000),
    FIVE_YEAR(157680000);

    private final int code;

    UserPrivacyUsagePeriodCode(int code) {
        this.code = code;
    }
}
