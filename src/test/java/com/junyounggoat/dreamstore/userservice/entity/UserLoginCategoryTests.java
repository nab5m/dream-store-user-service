package com.junyounggoat.dreamstore.userservice.entity;

import com.junyounggoat.dreamstore.userservice.constant.UserLoginCategoryCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.junyounggoat.dreamstore.userservice.entity.UserTests.createTestUser;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserLoginCategoryTests {
    @PersistenceContext
    private EntityManager entityManager;

    public static UserLoginCategory createTestUserLoginCategory(EntityManager entityManager, UserLoginCategoryCode userLoginCategoryCode) {
        User testUser = createTestUser(entityManager);

        UserLoginCategory testUserLoginCategory = UserLoginCategory.builder()
                .user(testUser)
                .userLoginCategoryCode(userLoginCategoryCode.getCode())
                .build();

        entityManager.persist(testUserLoginCategory);

        return testUserLoginCategory;
    }

    @Test
    @DisplayName("UserLoginCategory 엔티티 테이블 매핑 성공")
    public void userLoginCategoryEntityMappingSuccess() {
        // given
        UserLoginCategory testUserLoginCategory = createTestUserLoginCategory(entityManager, UserLoginCategoryCode.userLoginCredentials);

        // when
        UserLoginCategory foundUserLoginCategory = entityManager.find(UserLoginCategory.class, testUserLoginCategory.getUserLoginCategoryId());

        // then
        assertEquals(testUserLoginCategory.getUserLoginCategoryId(), foundUserLoginCategory.getUserLoginCategoryId());
    }
}
