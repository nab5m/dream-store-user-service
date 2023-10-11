package com.junyounggoat.dreamstore.userservice.sqs.listener;

import com.junyounggoat.dreamstore.userservice.constant.QueueName;
import com.junyounggoat.dreamstore.userservice.service.SendEventService;
import com.junyounggoat.dreamstore.userservice.service.UserService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FindPrivacyExpiredUserListener {
    private final UserService userService;
    private final SendEventService sendEventService;

    @SqsListener(QueueName.FIND_PRIVACY_EXPIRED_USER)
    public void listenFindPrivacyExpiredUser(@Payload String timestampString) {
        // 1. 만료된 사용자 목록 얻기
        List<Long> privacyExpiredUserIdList = userService.findPrivacyExpiredUser();

        // 2. sns 토픽 발송 ToDo: 양이 많을 때 어떻게 처리할지? userbatch에서 처리하는 편이 나을지도
        privacyExpiredUserIdList.forEach(sendEventService::sendEventExpireUserPrivacy);
    }
}
