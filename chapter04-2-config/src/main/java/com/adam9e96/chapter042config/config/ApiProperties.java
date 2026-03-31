package com.adam9e96.chapter042config.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * API 관련 설정을 타입 안전하게 바인딩.
 * <p>
 * {@link Duration}, {@link List} 등 다양한 타입의 자동 변환을 보여준다.
 *
 * <pre>
 * app.api:
 *   base-url: http://localhost:8080
 *   timeout: 5s          → Duration.ofSeconds(5)
 *   max-retries: 3       → int
 *   allowed-origins:     → List&lt;String&gt;
 *     - http://localhost:3000
 * </pre>
 */
@ConfigurationProperties(prefix = "app.api")
public record ApiProperties(
        String baseUrl,
        Duration timeout,
        int maxRetries,
        List<String> allowedOrigins
) {
}
