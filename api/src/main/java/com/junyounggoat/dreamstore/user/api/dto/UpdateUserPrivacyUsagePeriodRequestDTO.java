package com.junyounggoat.dreamstore.user.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateUserPrivacyUsagePeriodRequestDTO {
    @NotNull
    private Integer userPrivacyUsagePeriodCode;
}
