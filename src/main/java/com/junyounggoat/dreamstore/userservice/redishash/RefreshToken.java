package com.junyounggoat.dreamstore.userservice.redishash;

import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import static com.junyounggoat.dreamstore.userservice.constant.DateTimeConstants.REFRESH_TOKEN_VALID_SECONDS;

@RedisHash(value = "refreshToken")
@Builder
@Getter
public class RefreshToken {
    @Id
    private final String refreshToken;

    private final long userId;
}
