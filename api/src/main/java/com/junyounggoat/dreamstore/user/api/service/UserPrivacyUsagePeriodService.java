package com.junyounggoat.dreamstore.user.api.service;

import com.junyounggoat.dreamstore.user.api.dto.UpdateUserPrivacyUsagePeriodRequestDTO;
import com.junyounggoat.dreamstore.user.repository.entity.UserPrivacyUsagePeriod;
import com.junyounggoat.dreamstore.user.repository.UserRepository;
import com.junyounggoat.dreamstore.user.api.validation.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPrivacyUsagePeriodService {
    private final UserRepository userRepository;

    public void updateUserPrivacyUsagePeriod(Long userId, UpdateUserPrivacyUsagePeriodRequestDTO updateUserPrivacyUsagePeriodRequestDTO) {
        UserPrivacyUsagePeriod userPrivacyUsagePeriod = userRepository.findUserPrivacyUsagePeriodByUserId(userId);

        if (userPrivacyUsagePeriod == null) {
            throw new NotFoundException("기존 사용자개인정보사용기간을 찾을 수 없습니다.");
        }

        UserPrivacyUsagePeriod updateRequest = userPrivacyUsagePeriod.toBuilder()
                .userPrivacyUsagePeriodCode(updateUserPrivacyUsagePeriodRequestDTO.getUserPrivacyUsagePeriodCode())
                .usageStartDateTime(LocalDateTime.now())
                .build();

        userRepository.updateUserPrivacyPeriod(updateRequest);
    }
}
