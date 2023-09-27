package com.junyounggoat.dreamstore.userservice.constant;

import lombok.Getter;

@Getter
public enum UserLoginCategoryCode {
    userLoginCredentials(0),
    nonmember(1),
    kakaoUser(2),
    naverUser(3),
    smsSimpleAuth(4),
    emailAddressSimpleAuth(5);

    private final int code;

    UserLoginCategoryCode(int code) {
        this.code = code;
    }
}
