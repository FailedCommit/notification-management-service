package com.manning.notification.manager.services;

import com.manning.notification.manager.model.NotificationPreferencesResponse;
import com.manning.notification.manager.model.NotificationRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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

    @CircuitBreaker(name = "circuitBreakerService", fallbackMethod = "circuitBreakerFallback")
    public NotificationPreferencesResponse getNotificationPreferencesResponse(NotificationRequest request)  {
        final NotificationPreferencesResponse response = restTemplate.getForObject(
                notificationPreferencesUrl + "/" + request.getCustomerId(), NotificationPreferencesResponse.class);
        return response;
    }

    public NotificationPreferencesResponse circuitBreakerFallback(NotificationRequest request, Throwable throwable) {
        NotificationPreferencesResponse response = new NotificationPreferencesResponse();
        response.setStatus("WARNING");
        response.setStatusDescription("Circuit Breaker Opened for Notification Preferences Service");
        return response;
    }
}
