package com.junyounggoat.dreamstore.userservice.repository;

import com.junyounggoat.dreamstore.userservice.constant.SnsTopic;
import com.junyounggoat.dreamstore.userservice.dto.ExpireUserPrivacyEventDTO;
import io.awspring.cloud.sns.core.SnsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SnsNotificationRepository {
    private final SnsTemplate snsTemplate;

    public void sendEventExpireUserPrivacy(Long userId) {
        snsTemplate.sendNotification(SnsTopic.ExpireUserPrivacy.getArn(),
                ExpireUserPrivacyEventDTO.builder().userId(userId).build(),
                null);
    }
}
