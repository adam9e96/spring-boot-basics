package com.adam9e96.chapter022diioc.dto;

import com.adam9e96.chapter022diioc.model.Notification;

public record NotificationResponse(
        Long id,
        String channel,
        String recipient,
        String message,
        String sentAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getChannel(),
                notification.getRecipient(),
                notification.getMessage(),
                notification.getSentAt()
        );
    }
}
