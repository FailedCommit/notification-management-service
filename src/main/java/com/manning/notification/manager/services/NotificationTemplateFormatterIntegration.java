package com.manning.notification.manager.services;

import com.manning.notification.manager.model.NotificationGatewayRequest;
import com.manning.notification.manager.model.NotificationGatewayResponse;
import com.manning.notification.manager.model.NotificationRequest;
import com.manning.notification.manager.model.NotificationTemplateResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationTemplateFormatterIntegration {
    @Value("${uri.formatter.service}")
    private String notificationFormatterUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Bulkhead(name= "bulkheadService",fallbackMethod= "bulkheadFallback")
    @CircuitBreaker(name = "circuitBreakerService", fallbackMethod = "circuitBreakerFallback")
    @Retry(name= "retryService",fallbackMethod= "retryFallback")
    public NotificationTemplateResponse getNotificationTemplate(NotificationRequest request)  {
        ResponseEntity<NotificationTemplateResponse> response = restTemplate.postForEntity(
                notificationFormatterUrl, request, NotificationTemplateResponse.class);
        return response.getBody();
    }

    public NotificationTemplateResponse bulkheadFallback(NotificationRequest request, Throwable throwable) {
        NotificationTemplateResponse response = new NotificationTemplateResponse();
        response.setStatus("WARNING");
        response.setStatusDescription("Bulk Head for Notification Template Formatter Service");
        return response;
    }

    public NotificationTemplateResponse circuitBreakerFallback(NotificationRequest request, Throwable throwable) {
        NotificationTemplateResponse response = new NotificationTemplateResponse();
        response.setStatus("WARNING");
        response.setStatusDescription("Circuit Breaker Opened for Notification Template Formatter Service");
        return response;
    }

    public NotificationTemplateResponse retryFallback(NotificationRequest request, Throwable throwable) {
        NotificationTemplateResponse response = new NotificationTemplateResponse();
        response.setStatus("WARNING");
        response.setStatusDescription("Retry Failed for Notification Gateway Service");
        return response;
    }
}
