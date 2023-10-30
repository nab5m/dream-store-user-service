package com.junyounggoat.dreamstore.userservice.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

@TestConfiguration
public class EmbeddedRedisConfig {
    private final RedisServer redisServer;

    public EmbeddedRedisConfig() {
        this.redisServer = RedisServer.builder()
                .setting("maxmemory 128m")
                .build();
    }

    @PostConstruct
    public void startRedis() {
        try {
            this.redisServer.start();
        } catch (Exception e) {
            System.out.println("redis Server 실행 실패 : " + e.getMessage());
        }
    }

    @PreDestroy
    public void stopRedis() {
        this.redisServer.stop();
    }
}
