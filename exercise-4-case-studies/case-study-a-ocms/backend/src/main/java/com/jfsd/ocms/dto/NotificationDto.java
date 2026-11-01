package com.jfsd.ocms.dto;

import com.jfsd.ocms.model.Notification;

import java.time.LocalDateTime;

public record NotificationDto(Long id, String message, LocalDateTime createdAt, boolean read) {

    public static NotificationDto from(Notification n) {
        return new NotificationDto(n.getId(), n.getMessage(), n.getCreatedAt(), n.isRead());
    }
}
