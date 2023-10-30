package com.junyounggoat.dreamstore.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class NaverUserProfileResponseDTO {
    @JsonProperty("resultcode")
    private String resultCode;
    private String message;
    private ResponseDTO response;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class ResponseDTO {
        private String id;
        private String nickname;
        @JsonProperty("profile_image")
        private String profileImage;
        private String age;
        private String gender;
        private String email;
        private String mobile;
        private String mobile_e164;
        private String name;
        @JsonProperty("birthday")
        private String birthDay;
        @JsonProperty("birthyear")
        private String birthYear;
    }

    public String getNaverId() {
        return this.getResponse().getId();
    }
}
