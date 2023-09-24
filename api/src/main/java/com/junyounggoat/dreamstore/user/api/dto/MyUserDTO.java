package com.junyounggoat.dreamstore.user.api.dto;

import com.junyounggoat.dreamstore.user.repository.entity.User;
import com.junyounggoat.dreamstore.user.repository.entity.UserAgreementItem;
import com.junyounggoat.dreamstore.user.repository.entity.UserLoginCategory;
import com.junyounggoat.dreamstore.user.repository.entity.UserPrivacyUsagePeriod;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class MyUserDTO {
    private MyUserDTOUser user;
    private UserPrivacyUsagePeriodDTO userPrivacyUsagePeriod;
    private List<UserLoginCategoryDTO> userLoginCategoryList;
    private List<UserAgreementItemDTO> userAgreementItemList;

    @Getter
    public static class MyUserDTOUser {
        private final long userId;
        private final boolean userNonmemberFlag;
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
        public MyUserDTOUser(User user) {
            this.userId = user.getUserId();
            this.userNonmemberFlag = user.isUserNonmemberFlag();
            this.userPersonName = user.getUserPersonName();
            this.userEmailAddress = user.getUserEmailAddress();
            this.userPhoneNumber = user.getUserPhoneNumber();
            this.userNickname = user.getUserNickname();
            this.userGenderCode = user.getUserGenderCode();
            this.userBirthDate = user.getUserBirthDate();
            this.creationDateTime = user.getTimestamp().getCreationDateTime();
            this.lastUpdateDateTime = user.getTimestamp().getLastUpdateDateTime();
            this.deletionDateTime = user.getTimestamp().getDeletionDateTime();
        }
    }

    @Getter
    public static class UserLoginCategoryDTO {
        private final long userLoginCategoryId;
        private final int userLoginCategoryCode;
        private final LocalDateTime creationDateTime;
        private final LocalDateTime deletionDateTime;

        @Builder
        public UserLoginCategoryDTO(UserLoginCategory userLoginCategory) {
            this.userLoginCategoryId = userLoginCategory.getUserLoginCategoryId();
            this.userLoginCategoryCode = userLoginCategory.getUserLoginCategoryCode();
            this.creationDateTime = userLoginCategory.getCreationDateTime();
            this.deletionDateTime = userLoginCategory.getDeletionDateTime();
        }
    }

    @Getter
    public static class UserAgreementItemDTO {
        private final long userAgreementItemId;
        private final int userAgreementItemCode;
        private final LocalDateTime creationDateTime;
        private final LocalDateTime lastUpdateDateTime;
        private final LocalDateTime deletionDateTime;

        @Builder
        public UserAgreementItemDTO(UserAgreementItem userAgreementItem) {
            this.userAgreementItemId = userAgreementItem.getUserAgreementItemId();
            this.userAgreementItemCode = userAgreementItem.getUserAgreementItemCode();
            this.creationDateTime = userAgreementItem.getTimestamp().getCreationDateTime();
            this.lastUpdateDateTime = userAgreementItem.getTimestamp().getLastUpdateDateTime();
            this.deletionDateTime = userAgreementItem.getTimestamp().getDeletionDateTime();
        }
    }

    @Getter
    public static class UserPrivacyUsagePeriodDTO {
        private final long userPrivacyUsagePeriodId;
        private final int userPrivacyUsagePeriodCode;
        private final LocalDateTime usageStartDateTime;
        private final LocalDateTime creationDateTime;
        private final LocalDateTime lastUpdateDateTime;
        private final LocalDateTime deletionDateTime;

        @Builder
        public UserPrivacyUsagePeriodDTO(UserPrivacyUsagePeriod userPrivacyUsagePeriod) {
            this.userPrivacyUsagePeriodId = userPrivacyUsagePeriod.getUserPrivacyUsagePeriodId();
            this.userPrivacyUsagePeriodCode = userPrivacyUsagePeriod.getUserPrivacyUsagePeriodCode();
            this.usageStartDateTime = userPrivacyUsagePeriod.getUsageStartDateTime();
            this.creationDateTime = userPrivacyUsagePeriod.getTimestamp().getCreationDateTime();
            this.lastUpdateDateTime = userPrivacyUsagePeriod.getTimestamp().getLastUpdateDateTime();
            this.deletionDateTime = userPrivacyUsagePeriod.getTimestamp().getDeletionDateTime();
        }
    }
}
