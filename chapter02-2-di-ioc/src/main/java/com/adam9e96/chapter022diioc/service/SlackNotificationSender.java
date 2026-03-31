package com.adam9e96.chapter022diioc.service;

import lombok.extern.slf4j.Slf4j;

/**
 * Slack 알림 전송 구현체.
 * 스테레오타입 어노테이션 없이, {@code @Configuration} 클래스의 {@code @Bean} 메서드로 등록된다.
 *
 * @see com.adam9e96.chapter022diioc.config.NotificationConfig
 */
@Slf4j
public class SlackNotificationSender implements NotificationSender {

    @Override
    public String send(String recipient, String message) {
        log.info("[SLACK] {} 에게 전송: {}", recipient, message);
        return "Slack 전송 완료: " + recipient;
    }

    @Override
    public String getChannel() {
        return "SLACK";
    }
}
