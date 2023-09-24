package com.junyounggoat.dreamstore.user.api.dto;

import com.junyounggoat.dreamstore.user.repository.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class OtherUserDTO {
    private final long userId;
    private final String userPersonName;
    private final boolean userNonmemberFlag;
    private final String userNickname;
    private final Integer userGenderCode;
    private final LocalDate userBirthDate;
    private final LocalDateTime creationDateTime;
    private final LocalDateTime deletionDateTime;

    @Builder
    public OtherUserDTO(User user) {
        this.userId = user.getUserId();
        this.userPersonName = user.getUserPersonName();
        this.userNonmemberFlag = user.isUserNonmemberFlag();
        this.userNickname = user.getUserNickname();
        this.userGenderCode = user.getUserGenderCode();
        this.userBirthDate = user.getUserBirthDate();
        this.creationDateTime = user.getTimestamp().getCreationDateTime();
        this.deletionDateTime = user.getTimestamp().getDeletionDateTime();
    }
}
