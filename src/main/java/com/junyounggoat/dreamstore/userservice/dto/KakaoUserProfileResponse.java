package com.junyounggoat.dreamstore.userservice.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserProfileResponse {
    private Long id;
    private LocalDateTime connectedAt;
    private KakaoAccountDTO kakaoAccount;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KakaoAccountDTO {
        private Boolean profileNicknameNeedsAgreement;
        private Boolean profileImageNeedsAgreement;
        private ProfileDTO profile;

        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ProfileDTO {
            private String nickname;
            private String thumbnailImageUrl;
            private String profileImageUrl;
            private Boolean isDefaultImage;
        }
    }
}
