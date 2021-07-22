package com.manning.notification.manager.services;

import com.manning.notification.manager.model.NotificationGatewayRequest;
import com.manning.notification.manager.model.NotificationGatewayResponse;
import com.manning.notification.manager.model.NotificationPreferencesResponse;
import com.manning.notification.manager.model.NotificationRequest;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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

    @Bulkhead(name= "bulkheadService",fallbackMethod= "bulkheadFallback")
    @CircuitBreaker(name = "circuitBreakerService", fallbackMethod = "circuitBreakerFallback")
    @Retry(name= "retryService",fallbackMethod= "retryFallback")
    public NotificationPreferencesResponse getNotificationPreferencesResponse(NotificationRequest request)  {
        final NotificationPreferencesResponse response = restTemplate.getForObject(
                notificationPreferencesUrl + "/" + request.getCustomerId(), NotificationPreferencesResponse.class);
        return response;
    }

    public NotificationPreferencesResponse bulkheadFallback(NotificationRequest request, Throwable throwable) {
        NotificationPreferencesResponse response = new NotificationPreferencesResponse();
        response.setStatus("WARNING");
        response.setStatusDescription("Bulk Head for Notification Preferences Service");
        return response;
    }

    public NotificationPreferencesResponse circuitBreakerFallback(NotificationRequest request, Throwable throwable) {
        NotificationPreferencesResponse response = new NotificationPreferencesResponse();
        response.setStatus("WARNING");
        response.setStatusDescription("Circuit Breaker Opened for Notification Preferences Service");
        return response;
    }

    public NotificationPreferencesResponse retryFallback(NotificationRequest request, Throwable throwable) {
        NotificationPreferencesResponse response = new NotificationPreferencesResponse();
        response.setStatus("WARNING");
        response.setStatusDescription("Retry Failed for Notification Preferences Service");
        return response;
    }
}
