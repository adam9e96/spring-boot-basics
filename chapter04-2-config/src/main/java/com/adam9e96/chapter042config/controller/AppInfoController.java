package com.adam9e96.chapter042config.controller;

import com.adam9e96.chapter042config.dto.AppInfoResponse;
import com.adam9e96.chapter042config.dto.EnvironmentInfoResponse;
import com.adam9e96.chapter042config.dto.FeatureFlagsResponse;
import com.adam9e96.chapter042config.service.AppInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
@RequiredArgsConstructor
public class AppInfoController {

    private final AppInfoService appInfoService;

    /**
     * 앱 기본 정보 (이름, 버전, 설명, 연락처)
     */
    @GetMapping("/info")
    public AppInfoResponse getAppInfo() {
        return appInfoService.getAppInfo();
    }

    /**
     * 현재 환경 정보 (활성 프로파일, API 설정, greeting)
     */
    @GetMapping("/environment")
    public EnvironmentInfoResponse getEnvironment() {
        return appInfoService.getEnvironmentInfo();
    }

    /**
     * 기능 플래그 상태
     */
    @GetMapping("/features")
    public FeatureFlagsResponse getFeatures() {
        return appInfoService.getFeatureFlags();
    }
}
