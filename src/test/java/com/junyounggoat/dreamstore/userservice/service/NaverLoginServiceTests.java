package com.junyounggoat.dreamstore.userservice.service;

import com.junyounggoat.dreamstore.userservice.config.EmbeddedRedisConfig;
import com.junyounggoat.dreamstore.userservice.dto.NaverUserProfileResponseDTO;
import com.junyounggoat.dreamstore.userservice.dto.TokenResponseDTO;
import com.junyounggoat.dreamstore.userservice.entity.NaverUser;
import com.junyounggoat.dreamstore.userservice.validation.BadRequestException;
import com.junyounggoat.dreamstore.userservice.validation.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static com.junyounggoat.dreamstore.userservice.entity.NaverUserTests.createTestNaverUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@Import({ EmbeddedRedisConfig.class })
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class NaverLoginServiceTests {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private EmbeddedRedisConfig embeddedRedisConfig;

    @SpyBean
    private NaverLoginService naverLoginService;

    @BeforeAll
    public void startRedisServer() {
        embeddedRedisConfig.startRedis();
    }

    @AfterAll
    public void stopRedisServer() {
        embeddedRedisConfig.stopRedis();
    }

    @Test
    @DisplayName("네이버사용자 로그인 실패 : 유효하지 않은 accessToken")
    public void loginNaverUserFailsWhenAccessTokenIsInvalid() {
        assertThrows(BadRequestException.class, () -> naverLoginService.loginNaverUser(""));
    }

    @Test
    @DisplayName("네이버사용자 로그인 실패 : 가입하지 않은 네이버사용자")
    public void loginNaverUserFailsWhenNaverUserIsNotRegistered() {
        doReturn(NaverUserProfileResponseDTO.builder()
                .response(NaverUserProfileResponseDTO.ResponseDTO.builder()
                        .id("")
                        .build())
                .build()).when(naverLoginService).requestNaverUserProfile(anyString());

        assertThrows(NotFoundException.class, () -> naverLoginService.loginNaverUser(""));
    }

    @Test
    @DisplayName("네이버사용자 로그인 성공")
    public void loginNaverUserSuccess() {
        NaverUser naverUser = createTestNaverUser(entityManager);

        doReturn(NaverUserProfileResponseDTO.builder()
                .response(NaverUserProfileResponseDTO.ResponseDTO.builder()
                        .id(naverUser.getNaverId())
                        .build())
                .build()).when(naverLoginService).requestNaverUserProfile(anyString());

        TokenResponseDTO tokenResponseDTO = naverLoginService.loginNaverUser("");
        assertEquals(TokenService.getUserIdFromAccessToken(tokenResponseDTO.getAccessToken()),
                naverUser.getUserLoginCategory().getUser().getUserId());
    }
}
