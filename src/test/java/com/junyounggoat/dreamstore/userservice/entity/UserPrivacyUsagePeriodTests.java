package com.junyounggoat.dreamstore.userservice.entity;

import com.junyounggoat.dreamstore.userservice.constant.UserPrivacyUsagePeriodCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class UserPrivacyUsagePeriodTests {
    @PersistenceContext
    private EntityManager entityManager;

    public static UserPrivacyUsagePeriod createTestUserPrivacyUsagePeriod(EntityManager entityManager) {
        User testUser = UserTests.createTestUser(entityManager);

        UserPrivacyUsagePeriod testUserPrivacyUsagePeriod = UserPrivacyUsagePeriod.builder()
                .user(testUser)
                .userPrivacyUsagePeriodCode(UserPrivacyUsagePeriodCode.FOREVER.getCode())
                .usageStartDateTime(LocalDateTime.now())
                .build()
                .withUsageEndDateTime();

        entityManager.persist(testUserPrivacyUsagePeriod);

        return testUserPrivacyUsagePeriod;
    }

    @Test
    @DisplayName("UserPrivacyUsage 엔티티 테이블 매핑")
    public void userPrivacyUsageEntityMappingSuccess() {
        // given
        UserPrivacyUsagePeriod testUserPrivacyUsagePeriod = createTestUserPrivacyUsagePeriod(entityManager);

        // when
        UserPrivacyUsagePeriod foundUserPrivacyUsagePeriod =
                entityManager.find(UserPrivacyUsagePeriod.class, testUserPrivacyUsagePeriod.getUserPrivacyUsagePeriodId());

        // then
        assertEquals(testUserPrivacyUsagePeriod.getUserPrivacyUsagePeriodId(), foundUserPrivacyUsagePeriod.getUserPrivacyUsagePeriodId());
    }
}
