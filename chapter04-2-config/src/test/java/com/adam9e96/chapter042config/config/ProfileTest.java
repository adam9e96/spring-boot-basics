package com.adam9e96.chapter042config.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

class ProfileTest {

    @Nested
    @SpringBootTest
    @ActiveProfiles("dev")
    @DisplayName("dev 프로파일")
    class DevProfileTest {

        @Autowired
        private AppProperties appProperties;

        @Autowired
        private ApiProperties apiProperties;

        @Autowired
        private FeatureFlags featureFlags;

        @Test
        @DisplayName("dev 환경의 greeting이 적용된다")
        void dev_greeting() {
            assertThat(appProperties.greeting()).contains("DEV");
        }

        @Test
        @DisplayName("dev 환경의 timeout이 30초이다")
        void dev_timeout() {
            assertThat(apiProperties.timeout().getSeconds()).isEqualTo(30);
        }

        @Test
        @DisplayName("dev 환경에서 알림이 비활성화된다")
        void dev_notificationDisabled() {
            assertThat(featureFlags.notificationEnabled()).isFalse();
        }
    }

    @Nested
    @SpringBootTest
    @ActiveProfiles("prod")
    @DisplayName("prod 프로파일")
    class ProdProfileTest {

        @Autowired
        private AppProperties appProperties;

        @Autowired
        private ApiProperties apiProperties;

        @Autowired
        private FeatureFlags featureFlags;

        @Test
        @DisplayName("prod 환경의 greeting이 적용된다")
        void prod_greeting() {
            assertThat(appProperties.greeting()).contains("PROD");
        }

        @Test
        @DisplayName("prod 환경의 timeout이 3초이다")
        void prod_timeout() {
            assertThat(apiProperties.timeout().getSeconds()).isEqualTo(3);
        }

        @Test
        @DisplayName("prod 환경의 API base URL이 https이다")
        void prod_apiBaseUrl() {
            assertThat(apiProperties.baseUrl()).startsWith("https://");
        }

        @Test
        @DisplayName("prod 환경에서 알림이 활성화된다")
        void prod_notificationEnabled() {
            assertThat(featureFlags.notificationEnabled()).isTrue();
        }
    }
}
