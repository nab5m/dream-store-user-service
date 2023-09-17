package com.junyounggoat.dreamstore.userservice.repository;

import com.junyounggoat.dreamstore.userservice.entity.*;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final QUser qUser = QUser.user;
    private final QUserLoginCredentials qUserLoginCredentials = QUserLoginCredentials.userLoginCredentials;

    private final Predicate qUserIsNotDeleted = qUser.timestamp.deletionDateTime.isNull();
    private final Predicate qUserLoginCredentialsIsNotDeleted = qUserLoginCredentials.timestamp.deletionDateTime.isNull();

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
                .build();

        entityManager.persist(entity);

        return entity;
    }

    public UserLoginCredentials insertUserLoginCredentials(UserLoginCredentials userLoginCredentials) {
        UserLoginCredentials created = userLoginCredentials.toBuilder().build();
        entityManager.persist(created);

        return created;
    }

    public User findUserByUserEmailAddress(String userEmailAddress) {
        return queryFactory.selectFrom(qUser)
                .where(qUser.userEmailAddress.eq(userEmailAddress).and(qUserIsNotDeleted))
                .fetchOne();
    }

    public User findUserByUserPhoneNumber(String userPhoneNumber) {
        return queryFactory.selectFrom(qUser)
                .where(qUser.userPhoneNumber.eq(userPhoneNumber).and(qUserIsNotDeleted))
                .fetchOne();
    }

    public UserLoginCredentials findUserLoginCredentialsByLoginUserName(String loginUserName) {
        return queryFactory.selectFrom(qUserLoginCredentials)
                .where(qUserLoginCredentials.loginUserName.eq(loginUserName).and(qUserLoginCredentialsIsNotDeleted))
                .fetchOne();
    }
}
