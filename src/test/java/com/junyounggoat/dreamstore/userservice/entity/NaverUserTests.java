package com.junyounggoat.dreamstore.userservice.entity;

import com.junyounggoat.dreamstore.userservice.constant.UserLoginCategoryCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.junyounggoat.dreamstore.userservice.entity.UserLoginCategoryTests.createTestUserLoginCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class NaverUserTests {
    @PersistenceContext
    private EntityManager entityManager;

    public static NaverUser createTestNaverUser(EntityManager entityManager) {
        UserLoginCategory testUserLoginCategory = createTestUserLoginCategory(entityManager, UserLoginCategoryCode.naverUser);

        NaverUser testNaverUser = NaverUser.builder()
                .userLoginCategory(testUserLoginCategory)
                .naverId("1")
                .build();

        entityManager.persist(testNaverUser);

        return testNaverUser;
    }

    @Test
    @DisplayName("네이버사용자 엔티티 테이블 매핑 확인")
    public void naverUserEntityTableMappingSuccess() {
        // given
        NaverUser testNaverUser = createTestNaverUser(entityManager);

        // when
        NaverUser foundNaverUser = entityManager.find(NaverUser.class, testNaverUser.getNaverUserId());

        // then
        assertEquals(testNaverUser, foundNaverUser);
    }
}
