package com.junyounggoat.dreamstore.userservice.constant;

import lombok.Getter;

@Getter
public enum SnsTopic {
    ExpireUserPrivacy("arn:aws:sns:ap-northeast-2:856155157154:ExpireUserPrivacy.fifo", "ExpireUserPrivacy.fifo");

    private final String arn;
    private final String name;

    SnsTopic(String arn, String name) {
        this.arn = arn;
        this.name = name;
    }
}
