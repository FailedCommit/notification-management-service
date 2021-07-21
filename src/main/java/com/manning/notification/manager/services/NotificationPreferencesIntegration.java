package com.manning.notification.manager.services;

import com.manning.notification.manager.model.NotificationPreferencesResponse;
import com.manning.notification.manager.model.NotificationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationPreferencesIntegration {
    @Value("${url.preference.service}")
    private String notificationPreferencesUrl;

    @Autowired
    private RestTemplate restTemplate;

    public NotificationPreferencesResponse getNotificationPreferencesResponse(NotificationRequest request)  {
        final NotificationPreferencesResponse response = restTemplate.getForObject(
                notificationPreferencesUrl + "/" + request.getCustomerId(), NotificationPreferencesResponse.class);
        return response;
    }
}
