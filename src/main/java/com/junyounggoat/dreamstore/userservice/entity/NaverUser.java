package com.junyounggoat.dreamstore.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class NaverUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long naverUserId;

    @ManyToOne
    @JoinColumn(name = "naverUserLoginCategoryId", nullable = false)
    private UserLoginCategory userLoginCategory;

    @Size(max = 255)
    @Column(unique = true, nullable = false)
    private String naverId;

    @Size(max = 30)
    private String naverUserName;

    @Size(max = 30)
    private String naverUserNickname;

    @URL
    @Size(max = 512)
    private String naverUserProfileImageUrl;

    @Email
    @Size(max = 320)
    private String naverUserEmailAddress;

    private Integer naverUserBirthYear;

    @Size(max = 10)
    private String naverUserBirthDay;

    @Size(max = 15)
    private String naverUserPhoneNumber;

    @Size(max = 30)
    private String naverUserPhoneNumber_e164;

    @Size(max = 10)
    private String naverUserGender;

    @Embedded
    private TimestampEmbeddable timestamp;
}
