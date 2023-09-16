package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Builder(toBuilder = true)
@Getter
public class User {
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
    @NotBlank
    private String userPersonName;

    @Column(nullable = false, length = 320)
    @Size(max = 320)
    @NotBlank
    @Pattern(regexp = "^[\\w._%+-]+@[\\w._-]+\\.[\\w]{2,}$")
    private String userEmailAddress;

    @Column(nullable = false, length = 15)
    @NotBlank
    @Pattern(regexp = "^\\d{8,15}$")
    private String userPhoneNumber;

    @Column(length = 30)
    @Size(min = 1, max = 30)
    private String userNickname;

    /* ToDo: 사용자배송주소
    private UserShippingAddress userShippingAddress;
     */

    private Integer userGenderCode;

    private LocalDate userBirthDate;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
