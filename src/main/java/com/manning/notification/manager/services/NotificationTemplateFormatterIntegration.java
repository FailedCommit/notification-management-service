package com.manning.notification.manager.services;

import com.manning.notification.manager.model.NotificationPreferencesResponse;
import com.manning.notification.manager.model.NotificationRequest;
import com.manning.notification.manager.model.NotificationTemplateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationTemplateFormatterIntegration {
    @Value("${uri.formatter.service}")
    private String notificationFormatterUrl;

    @Autowired
    private RestTemplate restTemplate;

    public NotificationTemplateResponse getNotificationTemplate(NotificationRequest request)  {
        ResponseEntity<NotificationTemplateResponse> response = restTemplate.postForEntity(
                notificationFormatterUrl, request, NotificationTemplateResponse.class);
        return response.getBody();
    }
}
