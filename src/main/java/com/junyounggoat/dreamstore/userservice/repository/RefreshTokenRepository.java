package com.junyounggoat.dreamstore.userservice.repository;

import com.junyounggoat.dreamstore.userservice.redishash.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

import static com.junyounggoat.dreamstore.userservice.service.TokenService.REFRESH_TOKEN_VALID_SECONDS;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(final RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(refreshToken.getRefreshToken(), Long.valueOf(refreshToken.getUserId()).toString());
        redisTemplate.expire(refreshToken.getRefreshToken(), REFRESH_TOKEN_VALID_SECONDS, TimeUnit.SECONDS);
    }
}
