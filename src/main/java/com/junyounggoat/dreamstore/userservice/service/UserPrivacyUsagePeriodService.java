package com.junyounggoat.dreamstore.userservice.service;

import com.junyounggoat.dreamstore.userservice.dto.UpdateUserPrivacyUsagePeriodRequestDTO;
import com.junyounggoat.dreamstore.userservice.entity.UserPrivacyUsagePeriod;
import com.junyounggoat.dreamstore.userservice.repository.UserRepository;
import com.junyounggoat.dreamstore.userservice.validation.NotFoundException;
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
