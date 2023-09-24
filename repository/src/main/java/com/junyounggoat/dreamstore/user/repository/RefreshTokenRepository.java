package com.junyounggoat.dreamstore.user.repository;

import com.junyounggoat.dreamstore.user.repository.redishash.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

import static com.junyounggoat.dreamstore.user.repository.constant.DateTimeConstants.REFRESH_TOKEN_VALID_SECONDS;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {
    private final StringRedisTemplate redisTemplate;

    public void saveRefreshToken(final RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(refreshToken.getRefreshToken(), Long.valueOf(refreshToken.getUserId()).toString());
        redisTemplate.expire(refreshToken.getRefreshToken(), REFRESH_TOKEN_VALID_SECONDS, TimeUnit.SECONDS);
    }

    public @Nullable RefreshToken findById(final String refreshToken) {
        String userId = redisTemplate.opsForValue().get(refreshToken);
        if (userId == null) {
            return null;
        }

        // ToDo: NumberFormatException 예외처리
        return RefreshToken.builder()
                .userId(Long.parseLong(userId))
                .refreshToken(refreshToken)
                .build();
    }

    public Boolean deleteById(final String refreshToken) {
        return redisTemplate.delete(refreshToken);
    }
}
