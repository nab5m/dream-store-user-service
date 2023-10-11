package com.junyounggoat.dreamstore.userservice.constant;

import lombok.Getter;

@Getter
public enum SnsTopic {
    ExpireUserPrivacy("arn:aws:sns:ap-northeast-2:269669414396:ExpireUserPrivacy", "ExpireUserPrivacy");

    private final String arn;
    private final String name;

    SnsTopic(String arn, String name) {
        this.arn = arn;
        this.name = name;
    }
}
