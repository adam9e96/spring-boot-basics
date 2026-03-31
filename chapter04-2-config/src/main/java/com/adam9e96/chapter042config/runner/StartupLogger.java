package com.adam9e96.chapter042config.runner;

import com.adam9e96.chapter042config.config.AppProperties;
import com.adam9e96.chapter042config.config.ApiProperties;
import com.adam9e96.chapter042config.config.FeatureFlags;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 애플리케이션 시작 시 현재 설정값을 로그로 출력한다.
 * <p>
 * {@code CommandLineRunner}: 애플리케이션 구동 완료 후 자동 실행되는 콜백 인터페이스.
 * 로그 레벨(DEBUG, INFO, WARN 등)에 따라 출력 여부가 달라지는 것을 확인할 수 있다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StartupLogger implements CommandLineRunner {

    private final AppProperties appProperties;
    private final ApiProperties apiProperties;
    private final FeatureFlags featureFlags;
    private final Environment environment;

    @Override
    public void run(String... args) {
        log.info("===========================================");
        log.info("  애플리케이션 설정 정보");
        log.info("===========================================");
        log.info("앱 이름: {}", appProperties.name());
        log.info("버전: {}", appProperties.version());
        log.info("활성 프로파일: {}", String.join(", ",
                environment.getActiveProfiles().length > 0
                        ? environment.getActiveProfiles()
                        : new String[]{"default"}));
        log.info("API Base URL: {}", apiProperties.baseUrl());
        log.info("API Timeout: {}", apiProperties.timeout());
        log.info("알림 활성화: {}", featureFlags.notificationEnabled());
        log.info("유지보수 모드: {}", featureFlags.maintenanceMode());
        log.info("===========================================");

        // 로그 레벨별 출력 데모
        log.debug("이 메시지는 DEBUG 레벨 — dev 프로파일에서만 보입니다");
        log.info("이 메시지는 INFO 레벨 — 기본/dev에서 보입니다");
        log.warn("이 메시지는 WARN 레벨 — 모든 환경에서 보입니다");
    }
}
