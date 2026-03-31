package com.adam9e96.chapter022diioc.service;

import com.adam9e96.chapter022diioc.model.Notification;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 빈 생명주기(Lifecycle) 콜백 데모.
 * <p>
 * {@code @PostConstruct}: 빈 생성 + 의존성 주입 완료 후 호출
 * {@code @PreDestroy}: 빈 소멸 직전에 호출 (애플리케이션 종료 시)
 */
@Component
@Slf4j
public class NotificationLogger {

    @Getter
    private boolean initialized = false;

    @PostConstruct
    public void init() {
        initialized = true;
        log.info("========================================");
        log.info("NotificationLogger 빈이 초기화되었습니다");
        log.info("========================================");
    }

    @PreDestroy
    public void cleanup() {
        log.info("========================================");
        log.info("NotificationLogger 빈이 소멸됩니다");
        log.info("========================================");
    }

    public void log(Notification notification) {
        log.info("[LOG] [{}] {} → {}", notification.getChannel(), notification.getRecipient(), notification.getMessage());
    }
}
