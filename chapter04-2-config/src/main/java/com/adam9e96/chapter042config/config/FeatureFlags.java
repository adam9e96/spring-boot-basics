package com.adam9e96.chapter042config.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 기능 플래그(Feature Flag) 설정.
 * <p>
 * 프로파일별로 기능을 켜고 끌 수 있다.
 * 예: 개발 환경에서는 알림 비활성화, 운영 환경에서는 활성화.
 */
@ConfigurationProperties(prefix = "app.features")
public record FeatureFlags(
        boolean notificationEnabled,
        boolean maintenanceMode
) {
}
