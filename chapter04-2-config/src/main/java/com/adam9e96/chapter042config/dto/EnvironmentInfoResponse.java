package com.adam9e96.chapter042config.dto;

import java.util.List;

public record EnvironmentInfoResponse(
        List<String> activeProfiles,
        String greeting,
        String apiBaseUrl,
        String apiTimeout,
        int apiMaxRetries,
        List<String> allowedOrigins,
        boolean notificationEnabled,
        boolean maintenanceMode
) {
}
