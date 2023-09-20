package com.junyounggoat.dreamstore.userservice.util;

import com.junyounggoat.dreamstore.userservice.dto.AccessTokenResponseDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class JwtUtil {
    // ToDo: JWT_SIGNING_KEY 외부에서 가져오기
    private static final KeyPair JWT_SIGNING_KEY = Keys.keyPairFor(SignatureAlgorithm.RS256);
    private static final String JWT_CLAIM_USER_ID = "userId";
    private static final int ACCESS_TOKEN_VALID_MINUTES = 5;

    public static Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(JWT_SIGNING_KEY.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static String createAccessToken(Long userId) {
        // ToDo: 현재 날짜, Timezone 반영 여부 확인 필요
        Date today = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date expiration = Date.from(
                LocalDateTime.now().plusMinutes(ACCESS_TOKEN_VALID_MINUTES)
                        .atZone(ZoneId.systemDefault()).toInstant()
        );

        return Jwts.builder()
                .claim(JWT_CLAIM_USER_ID, userId)
                .setIssuedAt(today)
                .setExpiration(expiration)
                .signWith(JWT_SIGNING_KEY.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public static AccessTokenResponseDTO createAccessTokenResponse(Long userId) {
        return AccessTokenResponseDTO.builder()
                .accessToken(createAccessToken(userId))
                .build();
    }
}
