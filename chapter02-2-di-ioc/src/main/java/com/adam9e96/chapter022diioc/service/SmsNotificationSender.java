package com.adam9e96.chapter022diioc.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * SMS 알림 전송 구현체.
 * {@code @Component} 어노테이션으로 등록.
 * {@code @Service}와 기능적으로 동일하지만, 의미적으로 구분하기 위해 사용한다.
 */
@Component("smsSender")
@Slf4j
public class SmsNotificationSender implements NotificationSender {

    @Override
    public String send(String recipient, String message) {
        log.info("[SMS] {} 에게 전송: {}", recipient, message);
        return "SMS 전송 완료: " + recipient;
    }

    @Override
    public String getChannel() {
        return "SMS";
    }
}
