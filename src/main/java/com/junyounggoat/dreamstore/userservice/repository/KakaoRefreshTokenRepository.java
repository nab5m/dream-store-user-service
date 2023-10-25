package com.junyounggoat.dreamstore.userservice.repository;

import com.junyounggoat.dreamstore.userservice.redishash.KakaoRefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class KakaoRefreshTokenRepository {
    private final StringRedisTemplate redisTemplate;

    public void saveKakaoRefreshToken(final KakaoRefreshToken kakaoRefreshToken) {
        String key = Long.valueOf(kakaoRefreshToken.getKakaoId()).toString();
        redisTemplate.opsForValue().set(key, kakaoRefreshToken.getKakaoRefreshToken());
        redisTemplate.expire(key, Duration.ofDays(1L));
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
