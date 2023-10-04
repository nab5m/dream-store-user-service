package com.junyounggoat.dreamstore.userservice.dto;

import com.junyounggoat.dreamstore.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@EqualsAndHashCode
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
        if (user.getTimestamp() != null) {
            this.creationDateTime = user.getTimestamp().getCreationDateTime();
            this.deletionDateTime = user.getTimestamp().getDeletionDateTime();
        } else {
            this.creationDateTime = null;
            this.deletionDateTime = null;
        }
    }
}
