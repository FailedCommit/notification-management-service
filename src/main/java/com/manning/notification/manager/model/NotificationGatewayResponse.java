package com.manning.notification.manager.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NotificationGatewayResponse {
    private String status;
    private String statusDescription;
}
