package com.adam9e96.chapter042config.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AppPropertiesTest {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private ApiProperties apiProperties;

    @Autowired
    private FeatureFlags featureFlags;

    @Test
    @DisplayName("AppProperties가 application.yaml에서 올바르게 바인딩된다")
    void appProperties_binding() {
        assertThat(appProperties.name()).isEqualTo("Spring Boot 학습 프로젝트");
        assertThat(appProperties.version()).isEqualTo("1.0.0");
        assertThat(appProperties.description()).isNotBlank();
    }

    @Test
    @DisplayName("중첩 Record(Contact)가 올바르게 바인딩된다")
    void nestedRecord_binding() {
        assertThat(appProperties.contact()).isNotNull();
        assertThat(appProperties.contact().email()).isEqualTo("admin@example.com");
        assertThat(appProperties.contact().phone()).isEqualTo("02-1234-5678");
    }

    @Test
    @DisplayName("ApiProperties의 Duration과 List가 올바르게 바인딩된다")
    void apiProperties_binding() {
        assertThat(apiProperties.baseUrl()).isEqualTo("http://localhost:8080");
        assertThat(apiProperties.timeout()).isNotNull();
        assertThat(apiProperties.timeout().getSeconds()).isEqualTo(5);
        assertThat(apiProperties.maxRetries()).isEqualTo(3);
        assertThat(apiProperties.allowedOrigins()).containsExactly("http://localhost:3000");
    }

    @Test
    @DisplayName("FeatureFlags boolean 값이 올바르게 바인딩된다")
    void featureFlags_binding() {
        assertThat(featureFlags.notificationEnabled()).isTrue();
        assertThat(featureFlags.maintenanceMode()).isFalse();
    }
}
