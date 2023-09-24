package com.junyounggoat.dreamstore.user.api.dto;

import com.junyounggoat.dreamstore.user.repository.entity.User;
import com.junyounggoat.dreamstore.user.repository.validation.UserValidation;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class UpdateMyUserRequestDTO {
    @UserValidation.UserPersonName
    private final String userPersonName;

    @UserValidation.UserEmailAddress
    private final String userEmailAddress;

    @UserValidation.UserPhoneNumber
    private final String userPhoneNumber;

    @UserValidation.UserNickname
    private final String userNickname;

    @UserValidation.UserGenderCode
    private final Integer userGenderCode;

    @UserValidation.UserBirthDate
    private final LocalDate userBirthDate;

    public User updateUser(User currentUser) {
        return currentUser.toBuilder()
                .userPersonName(userPersonName)
                .userEmailAddress(userEmailAddress)
                .userPhoneNumber(userPhoneNumber)
                .userNickname(userNickname)
                .userGenderCode(userGenderCode)
                .userBirthDate(userBirthDate)
                .build();
    }
}
