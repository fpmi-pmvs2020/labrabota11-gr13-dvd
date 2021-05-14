package com.task.fbresult.model;

import java.time.LocalDateTime;

import lombok.Value;

@Value
public class AlertNotificationDTO {
    String type;
    String message;
    String senderId;

    String notifyTime;
}
