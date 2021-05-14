package com.task.fbresult.model;

import java.util.List;

import lombok.Value;

@Value
public class AlertInputDTO {
    List<AlertNotificationDTO> notifications;
}
