package com.junyounggoat.dreamstore.userservice.dto;

import com.junyounggoat.dreamstore.userservice.entity.User;
import com.junyounggoat.dreamstore.userservice.entity.UserLoginCredentials;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

// ToDo: 문서화 어노테이션 매핑을 DTO에 적용하는 건 어떨까?
@Builder
@Getter
public class CreateUserRequestDTO {
    @RequestDTOBlackList
    @Valid
    @NotNull
    private User user;

    @RequestDTOBlackList
    @Valid
    @NotNull
    private UserLoginCredentials userLoginCredentials;

    @NotEmpty
    private List<Integer> userAgreementItemCodeList;

    @NotNull
    private Integer userPrivacyUsagePeriodCode;
}
