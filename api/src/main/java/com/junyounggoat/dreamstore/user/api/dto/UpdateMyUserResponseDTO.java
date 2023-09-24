package com.junyounggoat.dreamstore.user.api.dto;

import com.junyounggoat.dreamstore.user.repository.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class UpdateMyUserResponseDTO {
    private final long userId;
    private final String userPersonName;
    private final String userEmailAddress;
    private final String userPhoneNumber;
    private final String userNickname;
    private final Integer userGenderCode;
    private final LocalDate userBirthDate;
    private final LocalDateTime creationDateTime;
    private final LocalDateTime lastUpdateDateTime;
    private final LocalDateTime deletionDateTime;

    @Builder
    public UpdateMyUserResponseDTO(User user) {
        userId = user.getUserId();
        userPersonName = user.getUserPersonName();
        userEmailAddress = user.getUserEmailAddress();
        userPhoneNumber = user.getUserPhoneNumber();
        userNickname = user.getUserNickname();
        userGenderCode = user.getUserGenderCode();
        userBirthDate = user.getUserBirthDate();
        creationDateTime = user.getTimestamp().getCreationDateTime();
        lastUpdateDateTime = user.getTimestamp().getLastUpdateDateTime();
        deletionDateTime = user.getTimestamp().getDeletionDateTime();
    }
}
