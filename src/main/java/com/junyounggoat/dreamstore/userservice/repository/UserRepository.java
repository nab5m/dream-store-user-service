package com.junyounggoat.dreamstore.userservice.repository;

import com.junyounggoat.dreamstore.userservice.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class UserRepository {
    @PersistenceContext
    private EntityManager entityManager;

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
}
