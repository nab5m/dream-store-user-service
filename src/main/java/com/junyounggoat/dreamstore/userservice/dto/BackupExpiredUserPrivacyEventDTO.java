package com.junyounggoat.dreamstore.userservice.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BackupExpiredUserPrivacyEventDTO {
    private final ExpiredUserPrivacy expiredUserPrivacy;

    @Builder
    @Getter
    public static class ExpiredUserPrivacy {
        private Long userId;
        private User user;
        private UserLoginCredentials userLoginCredentials;
        private String creationDateTime;

        @Builder
        @Getter
        public static class User {
            private String userPersonName;
        }

        @Builder
        @Getter
        public static class UserLoginCredentials {
            private String loginUserName;
        }
    }
}
