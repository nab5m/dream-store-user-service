package com.junyounggoat.dreamstore.user.repository.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static com.junyounggoat.dreamstore.user.repository.validation.UserValidation.*;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @UserId
    private long userId;

    @Column(nullable = false)
    private boolean userNonmemberFlag;

    /*
    @ManyToOne
    @JoinColumn(name = "activeUserLoginBlockPeriodId", nullable = false)
    private UserLoginBlockPeriod activeUserLoginBlockPeriod;
     */

    @Column(nullable = false, length = USER_PERSON_NAME_MAX_LENGTH)
    @UserPersonName
    private String userPersonName;

    @Column(nullable = false, length = USER_EMAIL_ADDRESS_MAX_LENGTH)
    @UserEmailAddress
    private String userEmailAddress;

    @Column(nullable = false, length = USER_PHONE_NUMBER_MAX_LENGTH)
    @UserPhoneNumber
    private String userPhoneNumber;

    @Column(length = USER_NICKNAME_MAX_LENGTH)
    @UserNickname
    private String userNickname;

    /* ToDo: 사용자배송주소
    private UserShippingAddress userShippingAddress;
     */

    @UserGenderCode
    private Integer userGenderCode;

    @UserBirthDate
    private LocalDate userBirthDate;

    @Embedded
    private TimestampEmbeddable timestamp;

    // ToDo: fk인 동시에 unique키 부여
    @OneToOne(mappedBy = "user")
    private UserPrivacyUsagePeriod userPrivacyUsagePeriod;

    @OneToMany(mappedBy = "user")
    private List<UserAgreementItem> userAgreementItemList;

    @OneToMany(mappedBy = "user")
    private List<UserLoginCategory> userLoginCategoryList;
}
