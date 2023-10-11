package com.junyounggoat.dreamstore.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ExpireUserPrivacyEventDTO {
    private Long userId;
}
