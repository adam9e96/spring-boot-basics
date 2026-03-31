package com.adam9e96.chapter022diioc.dto;

public record NotificationRequest(
        String recipient,
        String message
) {
}
