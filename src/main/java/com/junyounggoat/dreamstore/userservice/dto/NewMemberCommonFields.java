package com.junyounggoat.dreamstore.userservice.dto;

import java.util.List;

public interface NewMemberCommonFields {
    public CreateUserDTO getUser();

    public List<Integer> getUserAgreementItemCodeList();

    public Integer getUserPrivacyUsagePeriodCode();
}
