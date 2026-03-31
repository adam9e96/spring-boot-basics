package com.adam9e96.chapter042config.dto;

public record AppInfoResponse(
        String name,
        String version,
        String description,
        String contactEmail,
        String contactPhone
) {
}
