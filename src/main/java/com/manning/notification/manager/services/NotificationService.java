package com.manning.notification.manager.services;

import com.manning.notification.manager.model.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
@Data
public class NotificationService {
    private final NotificationPreferencesIntegration notificationPreferencesIntegration;
    private final NotificationGatewayIntegration notificationGatewayIntegration;
    private final NotificationTemplateFormatterIntegration notificationTemplateFormatterIntegration;

    public NotificationService(
            NotificationPreferencesIntegration notificationPreferencesIntegration,
            NotificationGatewayIntegration notificationGatewayIntegration,
            NotificationTemplateFormatterIntegration notificationTemplateFormatterIntegration) {
        this.notificationPreferencesIntegration = notificationPreferencesIntegration;
        this.notificationGatewayIntegration = notificationGatewayIntegration;
        this.notificationTemplateFormatterIntegration = notificationTemplateFormatterIntegration;
    }

    public NotificationResponse sendNotification(NotificationRequest request) {
        final NotificationGatewayRequest gatewayRequest = prepareNotificationGatewayRequest(request);
        final NotificationGatewayResponse notificationGatewayResponse = notificationGatewayIntegration.sendNotification(gatewayRequest);
        return prepareNotificationResponse(notificationGatewayResponse);
    }

    private NotificationGatewayRequest prepareNotificationGatewayRequest(NotificationRequest request) {
        NotificationPreferencesResponse preferences = notificationPreferencesIntegration.getNotificationPreferencesResponse(request);
        String notificationMode = preferences.isEmailPreferenceFlag() ? "EMAIL" : "SMS";
        request.setNotificationMode(notificationMode);
        NotificationTemplateResponse notificationTemplate = notificationTemplateFormatterIntegration.getNotificationTemplate(request);
        NotificationGatewayRequest gatewayRequest = new NotificationGatewayRequest();
        gatewayRequest.setCustomerId(request.getCustomerId());
        gatewayRequest.setNotificationMode(notificationMode);
        if ("EMAIL".equalsIgnoreCase(notificationMode)) {
            gatewayRequest.setEmailAddress(preferences.getEmailAddress());
            gatewayRequest.setEmailSubject(notificationTemplate.getEmailSubject());
            gatewayRequest.setNotificationContent(notificationTemplate.getEmailContent());
        } else {
            gatewayRequest.setPhoneNumber(preferences.getPhoneNumber());
            gatewayRequest.setNotificationContent(notificationTemplate.getSmsContent());
        }
        return gatewayRequest;
    }

    private NotificationResponse prepareNotificationResponse(NotificationGatewayResponse response) {
        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setStatus(response.getStatus());
        notificationResponse.setStatusDescription(response.getStatusDescription());
        notificationResponse.setNotificationReferenceId(UUID.randomUUID().toString());
        return notificationResponse;
    }
}
