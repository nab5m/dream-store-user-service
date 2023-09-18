package com.junyounggoat.dreamstore.userservice.constant;

import lombok.Getter;

@Getter
public enum CodeGroupName {
    REQUIRED_USER_AGREEMENT_ITEM("필수 사용자동의항목");

    private final String name;

    CodeGroupName(String name) {
        this.name = name;
    }
}
