package com.junyounggoat.dreamstore.user.repository.constant;

import lombok.Getter;

@Getter
public enum UserPrivacyUsagePeriodCode {
    FOREVER(0);

    private int code;

    UserPrivacyUsagePeriodCode(int code) {
        this.code = code;
    }
}
