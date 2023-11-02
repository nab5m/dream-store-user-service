package com.junyounggoat.dreamstore.userservice.service;

import com.junyounggoat.dreamstore.userservice.dto.NaverUserProfileResponseDTO;
import com.junyounggoat.dreamstore.userservice.dto.TokenResponseDTO;
import com.junyounggoat.dreamstore.userservice.entity.NaverUser;
import com.junyounggoat.dreamstore.userservice.entity.User;
import com.junyounggoat.dreamstore.userservice.entity.UserLoginCategory;
import com.junyounggoat.dreamstore.userservice.repository.UserRepository;
import com.junyounggoat.dreamstore.userservice.validation.BadRequestException;
import com.junyounggoat.dreamstore.userservice.validation.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class NaverLoginService {
    private final RestTemplate restTemplate;
    private final TokenService tokenService;
    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(NaverLoginService.class);

    public static final String REQUEST_NAVER_USER_PROFILE_FAILED = "네이버 사용자 조회에 실패했습니다. 문제가 지속될 경우 고객센터에 문의해주세요.";
    public static final String NAVER_USER_NOT_FOUND = "가입하지 않은 네이버 사용자입니다. 회원가입을 진행해주세요.";

    public NaverUserProfileResponseDTO requestNaverUserProfile(final String accessToken) throws BadRequestException {
        final String API_URL = "https://openapi.naver.com/v1/nid/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);

        HttpEntity<Object> request = new HttpEntity<>(httpHeaders);

        ResponseEntity<NaverUserProfileResponseDTO> response;
        try {
             response = restTemplate.exchange(API_URL, HttpMethod.GET, request, NaverUserProfileResponseDTO.class);
        } catch (Exception e) {
            logger.error("네이버 사용자 프로필 조회에 실패했습니다.", e);
            throw new BadRequestException(REQUEST_NAVER_USER_PROFILE_FAILED);
        }

        return response.getBody();
    }

    @Transactional(readOnly = true)
    public @Nullable User getUserByAccessToken(final String accessToken) {
        NaverUserProfileResponseDTO naverUserProfileResponseDTO = requestNaverUserProfile(accessToken);

        return getUserByNaverId(naverUserProfileResponseDTO.getNaverId());
    }

    @Transactional(readOnly = true)
    public @Nullable User getUserByNaverId(final String naverId) {
        return userRepository.findUserByNaverId(naverId);
    }

    public TokenResponseDTO loginNaverUser(final String accessToken) throws BadRequestException, NotFoundException {
        NaverUserProfileResponseDTO naverUserProfileResponseDTO = requestNaverUserProfile(accessToken);

        User user = getUserByNaverId(naverUserProfileResponseDTO.getNaverId());
        if (user == null) {
            throw new NotFoundException(NAVER_USER_NOT_FOUND);
        }

        return tokenService.createAccessTokenWithRefreshToken(user.getUserId());
    }

    public NaverUser createNaverUser(UserLoginCategory userLoginCategory, final String accessToken) throws BadRequestException {
        NaverUserProfileResponseDTO naverUserProfileResponseDTO = requestNaverUserProfile(accessToken);

        NaverUser naverUser = naverUserProfileResponseDTO.toNaverUser(userLoginCategory);
        return userRepository.insertNaverUser(naverUser);
    }
}
