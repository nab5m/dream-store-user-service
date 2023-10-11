package com.junyounggoat.dreamstore.userservice.service;

import com.junyounggoat.dreamstore.userservice.dto.BackupExpiredUserPrivacyEventDTO;
import com.junyounggoat.dreamstore.userservice.repository.SnsNotificationRepository;
import com.junyounggoat.dreamstore.userservice.repository.SqsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SendEventService {
    private final SnsNotificationRepository snsNotificationRepository;
    private final SqsRepository sqsRepository;

    public void sendEventExpireUserPrivacy(long userId) {
        snsNotificationRepository.sendEventExpireUserPrivacy(userId);
    }

    public void sendEventBackupExpiredUserPrivacy(BackupExpiredUserPrivacyEventDTO backupExpiredUserPrivacyEventDTO) {
        sqsRepository.sendEventBackupExpiredUserPrivacy(backupExpiredUserPrivacyEventDTO);
    }
}
