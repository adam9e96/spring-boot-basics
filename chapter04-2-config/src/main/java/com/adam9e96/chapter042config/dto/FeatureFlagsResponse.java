package com.adam9e96.chapter042config.dto;

public record FeatureFlagsResponse(
        boolean notificationEnabled,
        boolean maintenanceMode
) {
}
