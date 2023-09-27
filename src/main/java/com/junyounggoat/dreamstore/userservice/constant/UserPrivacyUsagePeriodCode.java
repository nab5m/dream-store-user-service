package com.junyounggoat.dreamstore.userservice.constant;

import lombok.Getter;

@Getter
public enum UserPrivacyUsagePeriodCode {
    FOREVER(0);

    private int code;

    UserPrivacyUsagePeriodCode(int code) {
        this.code = code;
    }
}
