package com.manning.notification.manager.model;

import lombok.Data;

import java.util.List;

@Data
public class NotificationRequest {
    private String customerId;
    private String notificationMode;
    private List<NotificationParameter> notificationParameters;
    private String notificationTemplateName;

    @Data
    public static class NotificationParameter {
        private String notificationParameterName;
        private String notificationParameterValue;
    }
}

