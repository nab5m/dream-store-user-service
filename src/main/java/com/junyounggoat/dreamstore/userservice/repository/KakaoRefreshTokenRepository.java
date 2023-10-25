package com.junyounggoat.dreamstore.userservice.repository;

import com.junyounggoat.dreamstore.userservice.redishash.KakaoRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class KakaoRefreshTokenRepository {
    private final StringRedisTemplate redisTemplate;

    public void saveKakaoRefreshToken(final KakaoRefreshToken kakaoRefreshToken) {
        redisTemplate.opsForValue().set(Long.valueOf(kakaoRefreshToken.getKakaoId()).toString(), kakaoRefreshToken.getKakaoRefreshToken());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true)
    public @Nullable KakaoRefreshToken findByKakaoId(final long kakaoId) {
        String kakaoRefreshToken = redisTemplate.opsForValue().get(Long.valueOf(kakaoId).toString());
        if (kakaoRefreshToken == null) {
            return null;
        }

        return KakaoRefreshToken.builder()
                .kakaoId(kakaoId)
                .kakaoRefreshToken(kakaoRefreshToken)
                .build();
    }
}
