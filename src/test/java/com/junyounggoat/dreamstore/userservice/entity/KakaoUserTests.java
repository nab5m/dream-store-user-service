package com.junyounggoat.dreamstore.userservice.entity;

import com.junyounggoat.dreamstore.userservice.constant.UserLoginCategoryCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.junyounggoat.dreamstore.userservice.entity.UserLoginCategoryTests.createTestUserLoginCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class KakaoUserTests {
    @PersistenceContext
    private EntityManager entityManager;

    public static KakaoUser createTestkakaoUser(EntityManager entityManager) {
        UserLoginCategory testUserLoginCategory = createTestUserLoginCategory(entityManager, UserLoginCategoryCode.kakaoUser);

        KakaoUser testKakaoUser = KakaoUser.builder()
                .userLoginCategory(testUserLoginCategory)
                .kakaoId(1L)
                .kakaoUserConnectionDateTime(LocalDateTime.now())
                .kakaoUserNickname("테스터")
                .kakaoUserThumbnailImageUrl("https://i.namu.wiki/i/EbHl4I2dCr3aoC7AFjMYv7zBAFQTE0Cr0-r2XiIKLakxARH3BY9eonE3AZ2_ctET_2vpLI-piN4F224wAUdyyQ.webp")
                .kakaoUserProfileImageUrl("https://i.namu.wiki/i/EbHl4I2dCr3aoC7AFjMYv7zBAFQTE0Cr0-r2XiIKLakxARH3BY9eonE3AZ2_ctET_2vpLI-piN4F224wAUdyyQ.webp")
                .kakaoUserDefaultImageFlag(false)
                .build();

        entityManager.persist(testKakaoUser);

        return testKakaoUser;
    }

    @Test
    @DisplayName("KakaoUser 엔티티 테이블 매핑 확인")
    public void kakaoUserEntityMappingSuccess() {
        // given
        KakaoUser testkakaoUser = createTestkakaoUser(entityManager);

        // when
        KakaoUser selectedKakaoUser = entityManager.find(KakaoUser.class, testkakaoUser.getKakaoUserId());

        // then
        assertEquals(testkakaoUser.getKakaoUserId(), selectedKakaoUser.getKakaoUserId());
    }
}
