package com.junyounggoat.dreamstore.userservice.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.junyounggoat.dreamstore.userservice.constant.QueueName;
import com.junyounggoat.dreamstore.userservice.dto.BackupExpiredUserPrivacyEventDTO;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Repository
@RequiredArgsConstructor
public class SqsRepository {
    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper;

    public void sendEventBackupExpiredUserPrivacy(BackupExpiredUserPrivacyEventDTO backupExpiredUserPrivacyEventDTO) {
        SqsTemplate sqsTemplate = SqsTemplate.builder()
                .sqsAsyncClient(sqsAsyncClient)
                .build();

        sqsTemplate.send(to -> {
            try {
                to.queue(QueueName.BACKUP_EXPIRED_USER_PRIVACY)
                        .payload(objectMapper.writeValueAsString(backupExpiredUserPrivacyEventDTO));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
