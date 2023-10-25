package com.junyounggoat.dreamstore.userservice.redishash;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "kakaoRefreshToken")
@Builder
@Getter
public class KakaoRefreshToken {
    @Id
    private long kakaoId;

    private String kakaoRefreshToken;
}
