package com.adam9e96.chapter042config.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * {@code @ConfigurationProperties}를 사용한 타입 안전(type-safe) 설정 바인딩.
 * <p>
 * {@code application.yaml}의 {@code app.*} 프로퍼티가 이 Record에 자동 매핑된다.
 * Record 기반이므로 불변(immutable)이고 별도의 setter가 필요 없다.
 *
 * <pre>
 * app:
 *   name: Spring Boot 학습 프로젝트
 *   version: 1.0.0
 *   description: ...
 *   contact:
 *     email: admin@example.com
 *     phone: 02-1234-5678
 * </pre>
 */
@ConfigurationProperties(prefix = "app")
public record AppProperties(
        String name,
        String version,
        String description,
        String greeting,
        Contact contact
) {
    /**
     * 중첩 Record — {@code app.contact.*} 프로퍼티에 매핑
     */
    public record Contact(String email, String phone) {
    }
}
