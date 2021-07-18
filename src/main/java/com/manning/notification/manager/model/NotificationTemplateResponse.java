package com.manning.notification.manager.model;

import lombok.Data;

@Data
public class NotificationTemplateResponse {
    private String status;
    private String statusDescription;
    private String emailSubject;
    private String emailContent;
    private String smsContent;
}
