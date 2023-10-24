package com.junyounggoat.dreamstore.userservice.service;

import com.junyounggoat.dreamstore.userservice.dto.KakaoTokenResponse;
import com.junyounggoat.dreamstore.userservice.dto.KakaoUserProfileResponse;
import com.junyounggoat.dreamstore.userservice.dto.TokenResponseDTO;
import com.junyounggoat.dreamstore.userservice.entity.User;
import com.junyounggoat.dreamstore.userservice.redishash.KakaoRefreshToken;
import com.junyounggoat.dreamstore.userservice.repository.KakaoRefreshTokenRepository;
import com.junyounggoat.dreamstore.userservice.validation.BadRequestException;
import com.junyounggoat.dreamstore.userservice.validation.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class KakaoLoginService {
    private final Logger logger = LoggerFactory.getLogger(KakaoLoginService.class);

    private final RestTemplate restTemplate;
    private final UserService userService;
    private final TokenService tokenService;
    private final KakaoRefreshTokenRepository kakaoRefreshTokenRepository;

    public static final String REQUEST_KAKAO_TOKEN_FAILED_MESSAGE = "카카오 엑세스 토큰 조회를 하지 못 했습니다.";
    public static final String REQUEST_KAKAO_PROFILE_FAILED_MESSAGE = "카카오 프로필 조회를 하지 못 했습니다.";

    @Value("${kakao.rest-api-key:}")
    private String KAKAO_REST_API_KEY;

    @Value("${kakao.login-redirect-uri:}")
    private String KAKAO_LOGIN_REDIRECT_URI;

    private @Nullable KakaoTokenResponse requestKakaoAccessToken(String authorizationCode)  {
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("grant_type", "authorization_code");
        data.add("client_id", KAKAO_REST_API_KEY);
        data.add("redirect_uri", KAKAO_LOGIN_REDIRECT_URI);
        data.add("code", authorizationCode);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);
        ResponseEntity<KakaoTokenResponse> response;

        try {
            final String apiUrl = "https://kauth.kakao.com/oauth/token";

            response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    request,
                    KakaoTokenResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("카카오 엑세스 토큰 요청 실패 : " + e.getMessage());
            return null;
        }

        return response.getBody();
    }

    private @Nullable KakaoUserProfileResponse requestKakaoUserProfile(String kakaoAccessToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("property_keys", "[\"kakao_account.profile\", \"kakao_account.name\", \"kakao_account.email\", \"kakao_account.age_range\", \"kakao_account.birthday\", \"kakao_account.gender\"]");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(kakaoAccessToken);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<KakaoUserProfileResponse> response;

        try {
            final String apiUrl = "https://kapi.kakao.com/v2/user/me";

            response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    request,
                    KakaoUserProfileResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            logger.error("카카오 사용자 정보 가져오기 요청 실패 : " + e.getMessage());
            return null;
        }

        return response.getBody();
    }

    public TokenResponseDTO loginKakaoUser(String authorizationCode) {
        KakaoTokenResponse kakaoTokenResponse = requestKakaoAccessToken(authorizationCode);
        if (kakaoTokenResponse == null) {
            logger.error("카카오 엑세스 토큰 조회 실패");
            throw new BadRequestException(REQUEST_KAKAO_TOKEN_FAILED_MESSAGE);
        }

        KakaoUserProfileResponse kakaoUserProfileResponse = requestKakaoUserProfile(kakaoTokenResponse.getAccessToken());
        if (kakaoUserProfileResponse == null) {
            logger.error("카카오 프로필 조회 실패");
            throw new BadRequestException(REQUEST_KAKAO_PROFILE_FAILED_MESSAGE);
        }

        Long kakaoId = kakaoUserProfileResponse.getId();
        User user = userService.findUserByKakaoId(kakaoId);
        if (user == null) {
            kakaoRefreshTokenRepository.saveKakaoRefreshToken(KakaoRefreshToken.builder()
                    .kakaoId(kakaoId)
                    .kakaoRefreshToken(kakaoTokenResponse.getRefreshToken())
                    .build());
            throw new NotFoundException(kakaoId.toString());
        }

        return tokenService.createAccessTokenWithRefreshToken(user.getUserId());
    }
}
