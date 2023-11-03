package com.junyounggoat.dreamstore.userservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateNaverUserRequestDTO implements NewMemberCommonFields {
    @Valid
    @NotNull
    private CreateUserDTO user;
    @NotBlank
    private String naverAccessToken;
    @NotEmpty
    private List<Integer> userAgreementItemCodeList;
    @NotNull
    private Integer userPrivacyUsagePeriodCode;
}
