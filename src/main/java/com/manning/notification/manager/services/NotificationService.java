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
    private final RestTemplate restTemplate;

    @Value("${url.gateway.service}")
    private String notificationGatewayUrl;
    @Value("${url.preference.service}")
    private String notificationPreferencesUrl;
    @Value("${uri.formatter.service}")
    private String notificationFormatterUrl;

    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NotificationResponse sendNotification(NotificationRequest request) {
        final NotificationGatewayRequest gatewayRequest = prepareNotificationGatewayRequest(request);
        return send(gatewayRequest);
    }

    private NotificationResponse send(NotificationGatewayRequest request) {
        ResponseEntity<NotificationGatewayResponse> response
                = restTemplate.postForEntity(notificationGatewayUrl, request, NotificationGatewayResponse.class);
        return prepareNotificationResponse(response);
    }

    private NotificationGatewayRequest prepareNotificationGatewayRequest(NotificationRequest request) {
        NotificationPreferencesResponse preferences = getNotificationPreferences(request.getCustomerId());
        NotificationTemplateResponse notificationTemplate = getNotificationTemplate(request);
        String notificationMode = preferences.isEmailPreferenceFlag() ? "EMAIL" : "SMS";
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

    private NotificationTemplateResponse getNotificationTemplate(NotificationRequest request) {
        ResponseEntity<NotificationTemplateResponse> response
                = restTemplate.postForEntity(notificationFormatterUrl, request, NotificationTemplateResponse.class);
        return response.getBody();
    }

    private NotificationPreferencesResponse getNotificationPreferences(String customerId) {
        NotificationPreferencesResponse response
                = restTemplate.getForObject(notificationPreferencesUrl + "/" + customerId, NotificationPreferencesResponse.class);
        return response;
    }

    private NotificationResponse prepareNotificationResponse(ResponseEntity<NotificationGatewayResponse> response) {
        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.setStatus(response.getBody().getStatus());
        notificationResponse.setStatusDescription(response.getBody().getStatusDescription());
        notificationResponse.setNotificationReferenceId(UUID.randomUUID().toString());
        return notificationResponse;
    }
}
