package com.junyounggoat.dreamstore.userservice.sqs.listener;

import com.junyounggoat.dreamstore.userservice.constant.QueueName;
import com.junyounggoat.dreamstore.userservice.dto.ExpireUserPrivacyEventDTO;
import com.junyounggoat.dreamstore.userservice.service.UserService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExpireUserPrivacyInUserServiceListener {
    private final UserService userService;

    @SqsListener(QueueName.EXPIRE_USER_PRIVACY_IN_USER_SERVICE)
    public void listenExpireUserPrivacyInUserService(@Payload ExpireUserPrivacyEventDTO expireUserPrivacyEventDTO) {
        userService.expireUserPrivacy(expireUserPrivacyEventDTO.getUserId());
    }
}
