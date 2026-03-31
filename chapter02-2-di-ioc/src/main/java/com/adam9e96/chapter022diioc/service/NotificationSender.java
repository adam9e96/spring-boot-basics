package com.adam9e96.chapter022diioc.service;

/**
 * 알림 전송 인터페이스.
 * 여러 구현체를 통해 DI/IoC 핵심 개념을 학습한다.
 */
public interface NotificationSender {

    /**
     * 알림을 전송한다.
     *
     * @param recipient 수신자
     * @param message   메시지 내용
     * @return 전송 결과 메시지
     */
    String send(String recipient, String message);

    /**
     * 전송 채널 이름을 반환한다.
     */
    String getChannel();
}
