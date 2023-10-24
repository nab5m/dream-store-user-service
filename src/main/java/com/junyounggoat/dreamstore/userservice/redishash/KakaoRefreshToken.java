package com.junyounggoat.dreamstore.userservice.redishash;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import static com.junyounggoat.dreamstore.userservice.constant.DateTimeConstants.ONE_DAY_SECONDS;

@RedisHash(value = "kakaoRefreshToken", timeToLive = ONE_DAY_SECONDS)
@Builder
@Getter
public class KakaoRefreshToken {
    @Id
    private long kakaoId;

    private String kakaoRefreshToken;
}
