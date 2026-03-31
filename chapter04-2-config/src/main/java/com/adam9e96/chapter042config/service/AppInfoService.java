package com.adam9e96.chapter042config.service;

import com.adam9e96.chapter042config.config.ApiProperties;
import com.adam9e96.chapter042config.config.AppProperties;
import com.adam9e96.chapter042config.config.FeatureFlags;
import com.adam9e96.chapter042config.dto.AppInfoResponse;
import com.adam9e96.chapter042config.dto.EnvironmentInfoResponse;
import com.adam9e96.chapter042config.dto.FeatureFlagsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * {@code @Value}와 {@code @ConfigurationProperties} 두 가지 방식의 설정 주입을 비교한다.
 * <p>
 * <b>{@code @Value}</b>: 단순한 값 하나를 주입할 때 사용. 기본값 지정 가능 ({@code ${key:default}})
 * <br>
 * <b>{@code @ConfigurationProperties}</b>: 관련 설정을 그룹으로 묶어 타입 안전하게 바인딩할 때 사용
 */
@Service
@RequiredArgsConstructor
public class AppInfoService {

    // @Value 방식: 단순 값 주입
    @Value("${app.greeting:안녕하세요}")
    private String greeting;

    // @ConfigurationProperties 방식: 타입 안전 바인딩
    private final AppProperties appProperties;
    private final ApiProperties apiProperties;
    private final FeatureFlags featureFlags;
    private final Environment environment;

    public AppInfoResponse getAppInfo() {
        return new AppInfoResponse(
                appProperties.name(),
                appProperties.version(),
                appProperties.description(),
                appProperties.contact().email(),
                appProperties.contact().phone()
        );
    }

    public EnvironmentInfoResponse getEnvironmentInfo() {
        List<String> activeProfiles = Arrays.asList(environment.getActiveProfiles());
        if (activeProfiles.isEmpty()) {
            activeProfiles = List.of("default");
        }

        return new EnvironmentInfoResponse(
                activeProfiles,
                greeting,
                apiProperties.baseUrl(),
                apiProperties.timeout().toString(),
                apiProperties.maxRetries(),
                apiProperties.allowedOrigins(),
                featureFlags.notificationEnabled(),
                featureFlags.maintenanceMode()
        );
    }

    public FeatureFlagsResponse getFeatureFlags() {
        return new FeatureFlagsResponse(
                featureFlags.notificationEnabled(),
                featureFlags.maintenanceMode()
        );
    }
}
