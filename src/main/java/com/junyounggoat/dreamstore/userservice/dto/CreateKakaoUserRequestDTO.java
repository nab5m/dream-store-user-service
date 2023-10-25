package com.junyounggoat.dreamstore.userservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CreateKakaoUserRequestDTO implements NewMemberCommonFields {
    @Valid
    @NotNull
    private CreateUserDTO user;

    @NotNull
    private Long kakaoId;

    @NotEmpty
    private List<Integer> userAgreementItemCodeList;

    @NotNull
    private Integer userPrivacyUsagePeriodCode;
}
