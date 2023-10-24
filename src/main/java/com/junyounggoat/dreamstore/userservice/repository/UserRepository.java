package com.junyounggoat.dreamstore.userservice.repository;

import com.junyounggoat.dreamstore.userservice.constant.UserLoginCategoryCode;
import com.junyounggoat.dreamstore.userservice.entity.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final QUser qUser = QUser.user;
    private final QUserLoginCategory qUserLoginCategory = QUserLoginCategory.userLoginCategory;
    private final QUserLoginCredentials qUserLoginCredentials = QUserLoginCredentials.userLoginCredentials;
    private final QUserPrivacyUsagePeriod qUserPrivacyUsagePeriod = QUserPrivacyUsagePeriod.userPrivacyUsagePeriod;
    private final QKakaoUser qKakaoUser = QKakaoUser.kakaoUser;

    private final BooleanExpression qUserIsNotDeleted = qUser.timestamp.deletionDateTime.isNull();
    private final BooleanExpression qUserLoginCategoryIsNotDeleted = qUserLoginCategory.deletionDateTime.isNull();
    private final BooleanExpression qUserLoginCredentialsIsNotDeleted = qUserLoginCredentials.timestamp.deletionDateTime.isNull();
    private final BooleanExpression qUserPrivacyUsagePeriodIsNotDeleted = qUserPrivacyUsagePeriod.timestamp.deletionDateTime.isNull();
    private final BooleanExpression qKakaoUserIsNotDeleted = qKakaoUser.timestamp.deletionDateTime.isNull();

    public User insertUser(User user) {
        User created = user.toBuilder().build();
        entityManager.persist(created);

        return created;
    }

    public UserLoginCategory insertUserLoginCategory(UserLoginCategory userLoginCategory) {
        UserLoginCategory created = userLoginCategory.toBuilder().build();
        entityManager.persist(created);

        return created;
    }

    public List<UserAgreementItem> insertUserAgreementItems(User user, List<Integer> userAgreementItemCodeList) {
        List<UserAgreementItem> entityList = new LinkedList<>();
        userAgreementItemCodeList.forEach(userAgreementItemCode -> {
            UserAgreementItem entity = UserAgreementItem.builder()
                    .user(user)
                    .userAgreementItemCode(userAgreementItemCode)
                    .build();

            entityManager.persist(entity);
            entityList.add(entity);
        });

        return entityList;
    }

    public UserPrivacyUsagePeriod insertUserPrivacyUsagePeriod(User user, int userPrivacyUsagePeriodCode) {
        UserPrivacyUsagePeriod entity = UserPrivacyUsagePeriod.builder()
                .user(user)
                .userPrivacyUsagePeriodCode(userPrivacyUsagePeriodCode)
                .usageStartDateTime(LocalDateTime.now())
                .build()
                .withUsageEndDateTime();

        entityManager.persist(entity);

        return entity;
    }

    public UserLoginCredentials insertUserLoginCredentials(UserLoginCredentials userLoginCredentials) {
        UserLoginCredentials created = userLoginCredentials.toBuilder().build();
        entityManager.persist(created);

        return created;
    }

    public @Nullable User findUserByUserEmailAddress(String userEmailAddress) {
        return queryFactory.selectFrom(qUser)
                .where(qUser.userEmailAddress.eq(userEmailAddress)
                        .and(qUserIsNotDeleted))
                .fetchOne();
    }

    public @Nullable User findUserByUserPhoneNumber(String userPhoneNumber) {
        return queryFactory.selectFrom(qUser)
                .where(qUser.userPhoneNumber.eq(userPhoneNumber)
                        .and(qUserIsNotDeleted))
                .fetchOne();
    }


    public UserLoginCredentials findUserLoginCredentialsByLoginUserName(String loginUserName) {
        return queryFactory.selectFrom(qUserLoginCredentials)
                .select(qUserLoginCredentials)
                .innerJoin(qUserLoginCredentials.userLoginCategory, qUserLoginCategory)
                .fetchJoin()
                .innerJoin(qUserLoginCategory.user, qUser)
                .fetchJoin()
                .where(qUserLoginCredentials.loginUserName.eq(loginUserName)
                        .and(qUserLoginCredentialsIsNotDeleted)
                        .and(qUserLoginCategoryIsNotDeleted)
                        .and(qUserIsNotDeleted))
                .fetchOne();
    }

    public @Nullable User findUserByUserId(Long userId) {
        return queryFactory.selectFrom(qUser)
                .where(qUser.userId.eq(userId).and(qUserIsNotDeleted))
                .fetchOne();
    }

    public User updateUser(User user) {
        User updated = user.toBuilder().build();
        entityManager.merge(updated);

        return updated;
    }

    public UserPrivacyUsagePeriod updateUserPrivacyPeriod(UserPrivacyUsagePeriod userPrivacyUsagePeriod) {
        UserPrivacyUsagePeriod updated = userPrivacyUsagePeriod.withUsageEndDateTime();
        entityManager.merge(updated);

        return updated;
    }

    public UserLoginCredentials updateUserLoginCredentials(UserLoginCredentials userLoginCredentials) {
        UserLoginCredentials updated = userLoginCredentials.toBuilder().build();
        entityManager.merge(updated);

        return updated;
    }

    public @Nullable UserPrivacyUsagePeriod findUserPrivacyUsagePeriodByUserId(Long userId) {
        return queryFactory.selectFrom(qUserPrivacyUsagePeriod)
                .innerJoin(qUserPrivacyUsagePeriod.user, qUser)
                .where(qUser.userId.eq(userId)
                        .and(qUserIsNotDeleted)
                        .and(qUserPrivacyUsagePeriodIsNotDeleted))
                .fetchOne();
    }

    public @Nullable User findUserByUserNickname(String userNickname) {
        return queryFactory.selectFrom(qUser)
                .where(qUser.userNickname.eq(userNickname)
                        .and(qUserIsNotDeleted))
                .fetchOne();
    }

    public List<Long> findPrivacyExpiredUser() {
        return queryFactory.selectFrom(qUserPrivacyUsagePeriod)
                .select(qUser.userId)
                .innerJoin(qUserPrivacyUsagePeriod.user)
                .where(qUserPrivacyUsagePeriod.usageEndDateTime.before(LocalDateTime.now())
                        .and(qUser.privacyExpirationCompleteDateTime.isNull()))
                .fetch();
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UserPrivacy {
        private User user;
        @Nullable
        private UserLoginCredentials userLoginCredentials;
    }

    public UserPrivacy getUserPrivacy(Long userId) {
        return queryFactory.selectFrom(qUser)
                .select(Projections.fields(UserPrivacy.class, qUser.as("user"),
                        qUserLoginCredentials.as("userLoginCredentials")))
                .leftJoin(qUserLoginCategory)
                .on(qUser.userId.eq(qUserLoginCategory.user.userId)
                        .and(qUserLoginCategory.userLoginCategoryCode.eq(UserLoginCategoryCode.userLoginCredentials.getCode()))
                        .and(qUserLoginCategoryIsNotDeleted))
                .leftJoin(qUserLoginCredentials)
                .on(qUserLoginCategory.userLoginCategoryId.eq(qUserLoginCredentials.userLoginCategory.userLoginCategoryId)
                        .and(qUserLoginCredentialsIsNotDeleted))
                .where(qUser.userId.eq(userId)
                        .and(qUserIsNotDeleted))
                .fetchOne();
    }

    public User findUserByKakaoId(Long kakaoId) {
        return queryFactory.selectFrom(qKakaoUser)
                .select(qUser)
                .innerJoin(qKakaoUser.userLoginCategory, qUserLoginCategory)
                .innerJoin(qUserLoginCategory.user, qUser)
                .where(qKakaoUser.kakaoId.eq(kakaoId)
                        .and(qKakaoUserIsNotDeleted)
                        .and(qUserLoginCategoryIsNotDeleted)
                        .and(qUserIsNotDeleted))
                .fetchOne();
    }
}
