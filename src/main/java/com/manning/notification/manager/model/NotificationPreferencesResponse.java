package com.manning.notification.manager.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationPreferencesResponse {
    private String status;
    private String statusDescription;
    private boolean smsPreferenceFlag;
    private boolean emailPreferenceFlag;
    private String emailAddress;
    private String phoneNumber;
}
