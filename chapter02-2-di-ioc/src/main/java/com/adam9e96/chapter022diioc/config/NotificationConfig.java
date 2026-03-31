package com.adam9e96.chapter022diioc.config;

import com.adam9e96.chapter022diioc.service.NotificationSender;
import com.adam9e96.chapter022diioc.service.SlackNotificationSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code @Configuration} + {@code @Bean}을 사용한 수동 빈 등록 예제.
 * <p>
 * {@link SlackNotificationSender}는 스테레오타입 어노테이션({@code @Component} 등)이 없으므로
 * 컴포넌트 스캔으로 자동 등록되지 않는다. 이 설정 클래스에서 직접 빈으로 등록한다.
 */
@Configuration
public class NotificationConfig {

    @Bean("slackSender")
    public NotificationSender slackNotificationSender() {
        return new SlackNotificationSender();
    }
}
