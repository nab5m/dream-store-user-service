package com.junyounggoat.dreamstore.userservice.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserTests {
    @PersistenceContext
    private EntityManager entityManager;

    public static User createTestUser(EntityManager entityManager) {
        User testUser = User.builder()
                .userNonmemberFlag(true)
                .build();

        entityManager.persist(testUser);

        return testUser;
    }

    @Test
    @DisplayName("User 엔티티 테이블 매핑 확인")
    public void userEntityMappingSuccess() {
        // given
        User testUser = createTestUser(entityManager);

        // when
        User foundUser = entityManager.find(User.class, testUser.getUserId());

        // then
        assertEquals(testUser.getUserId(), foundUser.getUserId());
    }
}
