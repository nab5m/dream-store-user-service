package com.junyounggoat.dreamstore.userservice.dto;

import com.junyounggoat.dreamstore.userservice.entity.User;
import com.junyounggoat.dreamstore.userservice.validation.UserValidation;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CreateUserDTO {
    @UserValidation.UserPersonNameNotBlank
    private String userPersonName;

    @UserValidation.UserEmailAddressNotBlank
    private String userEmailAddress;

    @UserValidation.UserPhoneNumberNotBlank
    private String userPhoneNumber;

    public User.UserBuilder toUserBuilder() {
        return User.builder()
                .userPersonName(userPersonName)
                .userEmailAddress(userEmailAddress)
                .userPhoneNumber(userPhoneNumber);
    }
}
