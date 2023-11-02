package com.junyounggoat.dreamstore.userservice.service;

import com.junyounggoat.dreamstore.userservice.dto.CreateUserDTO;
import com.junyounggoat.dreamstore.userservice.dto.TokenResponseDTO;
import com.junyounggoat.dreamstore.userservice.entity.NaverUser;
import com.junyounggoat.dreamstore.userservice.entity.UserLoginCategory;
import com.junyounggoat.dreamstore.userservice.validation.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.junyounggoat.dreamstore.userservice.service.NaverLoginService.REQUEST_NAVER_USER_PROFILE_FAILED;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
@Transactional
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @SpyBean
    private NaverLoginService naverLoginService;

    private TokenResponseDTO createNaverUser() {
        CreateNaverUserRequestDTO createNaverUserRequestDTO = CreateNaverUserRequestDTO.builder()
                .user(CreateUserDTO.builder()
                        .userEmailAddress("tester1@tester.com")
                        .userPersonName("테스터")
                        .userPhoneNumber("01000000000")
                        .build())
                .naverAccessToken("testNaverUser")
                .userPrivacyUsagePeriodCode(0)
                .userAgreementItemCodeList(List.of())
                .build();

        return userService.createNaverUser(createNaverUserRequestDTO);
    }

    @Test
    @DisplayName("네이버사용자 생성 : 성공")
    public void createNaverUserSuccess() {
        // given
        doReturn(NaverUser.builder()
                .build())
                .when(naverLoginService).createNaverUser(any(UserLoginCategory.class), anyString());

        // when
        TokenResponseDTO tokenResponseDTO = createNaverUser();

        // then
        assertNotNull(tokenResponseDTO);
    }

    @Test
    @DisplayName("네이버사용자 생성 : 네이버사용자 프로필 조회 실패")
    public void createNaverUserFailsWhenRequestNaverUserFails() {
        // given
        doThrow(new BadRequestException(REQUEST_NAVER_USER_PROFILE_FAILED))
                .when(naverLoginService.createNaverUser(any(UserLoginCategory.class), anyString()));

        // when
        Executable createNaverUserExecutable = this::createNaverUser;

        // then
        assertThrows(BadRequestException.class, createNaverUserExecutable);
    }
}
