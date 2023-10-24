package com.junyounggoat.dreamstore.userservice.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class KakaoLoginServiceTests {
    @Autowired
    private KakaoLoginService kakaoLoginService;

    @Test
    @DisplayName("승인코드로 카카오 사용자 조회")
    public void findKakaoUserByAuthorizationCode() {
        kakaoLoginService.loginKakaoUser("");
    }
}
