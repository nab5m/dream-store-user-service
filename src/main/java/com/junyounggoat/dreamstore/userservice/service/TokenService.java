package com.junyounggoat.dreamstore.userservice.service;

import com.junyounggoat.dreamstore.userservice.dto.TokenResponseDTO;
import com.junyounggoat.dreamstore.userservice.redishash.RefreshToken;
import com.junyounggoat.dreamstore.userservice.repository.RefreshTokenRepository;
import com.junyounggoat.dreamstore.userservice.validation.UnAuthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.junyounggoat.dreamstore.userservice.validation.UnAuthorizedException.LOGIN_REQUIRED_ERROR_MESSAGE;
import static com.junyounggoat.dreamstore.userservice.constant.DateTimeConstants.REFRESH_TOKEN_VALID_SECONDS;


@Service
@RequiredArgsConstructor
@Transactional
public class TokenService {
    // ToDo: JWT_SIGNING_KEY 외부에서 가져오기
    private static final KeyPair JWT_SIGNING_KEY = Keys.keyPairFor(SignatureAlgorithm.RS256);
    public static final String JWT_CLAIM_USER_ID = "userId";
    private static final int ACCESS_TOKEN_VALID_MINUTES = 5;

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private final RefreshTokenRepository refreshTokenRepository;

    public static @Nullable Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(JWT_SIGNING_KEY.getPublic())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | ExpiredJwtException | IllegalArgumentException e) {
            logger.info(e.getMessage());
        }

        return null;
    }

    public String createAccessToken(long userId) {
        // ToDo: 현재 날짜, Timezone 반영 여부 확인 필요
        // ToDo: Claims에 roles 부여하기
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

    private String createRefreshToken(long userId) {
        Date today = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date expiration = Date.from(
                LocalDateTime.now().plusSeconds(REFRESH_TOKEN_VALID_SECONDS)
                        .atZone(ZoneId.systemDefault()).toInstant()
        );

        String refreshTokenString = Jwts.builder()
                .claim(JWT_CLAIM_USER_ID, userId)
                .setIssuedAt(today)
                .setExpiration(expiration)
                .signWith(JWT_SIGNING_KEY.getPrivate(), SignatureAlgorithm.RS256)
                .compact();

        RefreshToken refreshToken = RefreshToken.builder()
                .refreshToken(refreshTokenString)
                .userId(userId)
                .build();

        refreshTokenRepository.saveRefreshToken(refreshToken);

        return refreshTokenString;
    }

    public TokenResponseDTO createAccessTokenWithRefreshToken(long userId) {
        return TokenResponseDTO.builder()
                .accessToken(createAccessToken(userId))
                .refreshToken(createRefreshToken(userId))
                .build();
    }

    @Transactional(readOnly = true)
    public @Nullable RefreshToken getRefreshToken(String refreshToken) {
        return refreshTokenRepository.findById(refreshToken);
    }

    public Boolean deleteRefreshToken(final String refreshToken) {
        return refreshTokenRepository.deleteById(refreshToken);
    }

    public static Long getUserIdFromUserDetails(@Nullable UserDetails userDetails) {
        // ToDo: 예외처리를 더 깔끔하게 할 수 없을까?
        if (userDetails == null) {
            throw new UnAuthorizedException(LOGIN_REQUIRED_ERROR_MESSAGE);
        }

        String token = userDetails.getPassword();
        Claims claims = getClaims(token);
        if (claims == null) {
            throw new UnAuthorizedException(LOGIN_REQUIRED_ERROR_MESSAGE);
        }

        Long userId;
        try {
            userId = claims.get(JWT_CLAIM_USER_ID, Long.class);
        } catch (RequiredTypeException e) {
            logger.info("TypeCastUserId Failed : " + token);

            throw new UnAuthorizedException(LOGIN_REQUIRED_ERROR_MESSAGE);
        }

        return userId;
    }
}
