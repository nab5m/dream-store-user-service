package com.junyounggoat.dreamstore.userservice.repository;

import static com.junyounggoat.dreamstore.userservice.entity.UserPrivacyUsagePeriodTests.createTestUserPrivacyUsagePeriod;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.junyounggoat.dreamstore.userservice.constant.UserPrivacyUsagePeriodCode;
import com.junyounggoat.dreamstore.userservice.entity.User;
import com.junyounggoat.dreamstore.userservice.entity.UserPrivacyUsagePeriod;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;
    
    @Test
    @DisplayName("개인정보사용기간이 만료된 사용자 찾기")
    public void findPrivacyExpiredUser() {
        // given
        // 개인정보사용기간이 지나지 않은 사용자
        UserPrivacyUsagePeriod privacyNotExpiredUser = createTestUserPrivacyUsagePeriod(entityManager);

        // 개인정보사용기간이 지났고 아직 만료 처리가 되지 않은 사용자
        UserPrivacyUsagePeriod privacyExpiredAndNeedsRemove =
                createTestUserPrivacyUsagePeriod(entityManager).toBuilder()
                        .usageStartDateTime(LocalDateTime.now().minusYears(10))
                        .userPrivacyUsagePeriodCode(UserPrivacyUsagePeriodCode.ONE_YEAR.getCode())
                        .build()
                        .withUsageEndDateTime();
        entityManager.merge(privacyExpiredAndNeedsRemove);

        // 개인정보사용기간이 지났지만 만료 처리가 완료된 사용자
        UserPrivacyUsagePeriod privacyExpiredAndRemoved =
                createTestUserPrivacyUsagePeriod(entityManager).toBuilder()
                        .usageStartDateTime(LocalDateTime.now().minusYears(10))
                        .userPrivacyUsagePeriodCode(UserPrivacyUsagePeriodCode.ONE_YEAR.getCode())
                        .build()
                        .withUsageEndDateTime();
        User privacyExpiredAndRemovedUser = privacyExpiredAndRemoved.getUser().toBuilder()
                .privacyExpirationCompleteDateTime(LocalDateTime.now())
                .build();

        privacyExpiredAndRemoved = privacyExpiredAndRemoved.toBuilder()
                .user(privacyExpiredAndRemovedUser)
                .build();

        entityManager.merge(privacyExpiredAndRemovedUser);
        entityManager.merge(privacyExpiredAndRemoved);

        // when
        List<Long> userIdList = userRepository.findPrivacyExpiredUser();

        // then
        assertEquals(userIdList, List.of(privacyExpiredAndNeedsRemove.getUser().getUserId()));
    }
}
