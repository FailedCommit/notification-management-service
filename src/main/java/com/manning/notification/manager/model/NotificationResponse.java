package com.manning.notification.manager.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class NotificationResponse {
    private String status;
    private String statusDescription;
    //TODO: Need to understand the use-case for this. For now changing it to String from Long
    private String notificationReferenceId;
}
