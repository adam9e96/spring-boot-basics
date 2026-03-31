package com.adam9e96.chapter022diioc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 이메일 알림 전송 구현체.
 * {@code @Service} 어노테이션으로 컴포넌트 스캔을 통해 빈으로 등록된다.
 */
@Service("emailSender")
@Slf4j
public class EmailNotificationSender implements NotificationSender {

    @Override
    public String send(String recipient, String message) {
        log.info("[EMAIL] {} 에게 전송: {}", recipient, message);
        return "이메일 전송 완료: " + recipient;
    }

    @Override
    public String getChannel() {
        return "EMAIL";
    }
}
