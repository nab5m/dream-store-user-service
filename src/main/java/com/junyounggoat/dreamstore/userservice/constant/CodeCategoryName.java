package com.junyounggoat.dreamstore.userservice.constant;

import lombok.Getter;

@Getter
public enum CodeCategoryName {
    USER_AGREEMENT_ITEM("사용자동의항목"),
    USER_PRIVACY_USAGE_PERIOD("사용자개인정보사용기간"),
    GENDER("성별");

    private final String name;

    CodeCategoryName(String name) {
        this.name = name;
    }
}
