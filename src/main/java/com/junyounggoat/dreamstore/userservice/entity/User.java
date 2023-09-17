package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.junyounggoat.dreamstore.userservice.validation.UserValidation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static com.junyounggoat.dreamstore.userservice.validation.UserValidation.*;

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

    private Integer userGenderCode;

    private LocalDate userBirthDate;

    @Embedded
    private TimestampEmbeddable timestamp;
}
