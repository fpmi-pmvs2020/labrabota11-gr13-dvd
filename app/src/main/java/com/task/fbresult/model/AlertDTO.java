package com.task.fbresult.model;

import lombok.Value;

@Value
public class AlertDTO {
    String type;
    String message;
    String personId;
}
