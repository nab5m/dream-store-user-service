package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long userId;

    @Column(nullable = false)
    private boolean userNonmemberFlag;

    /*
    @ManyToOne
    @JoinColumn(name = "activeUserLoginBlockPeriodId", nullable = false)
    private UserLoginBlockPeriod activeUserLoginBlockPeriod;
     */

    @Column(nullable = false, length = 30)
    @Size(min = 2, max = 30)
    private String userPersonName;

    @Column(nullable = false, length = 320)
    @Size(max = 320)
    @Pattern(regexp = "^[\\w._%+-]+@[\\w._-]+\\.[\\w]{2,}$")
    private String userEmailAddress;

    @Column(nullable = false, length = 15)
    @Pattern(regexp = "^\\d{8,15}$")
    private String userPhoneNumber;

    @Column(length = 30)
    @Size(max = 30)
    private String userNickname;

    /* ToDo: 사용자배송주소
    private UserShippingAddress userShippingAddress;
     */

    private int userGenderCode;

    private LocalDate userBirthDate;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
