package com.manning.notification.manager.services;

import com.manning.notification.manager.model.NotificationGatewayRequest;
import com.manning.notification.manager.model.NotificationGatewayResponse;
import com.manning.notification.manager.model.NotificationPreferencesResponse;
import com.manning.notification.manager.model.NotificationRequest;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificationGatewayIntegration {
    @Value("${url.gateway.service}")
    private String notificationGatewayUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Bulkhead(name= "bulkheadService",fallbackMethod= "bulkheadFallback")
    @CircuitBreaker(name = "circuitBreakerService", fallbackMethod = "circuitBreakerFallback")
    public NotificationGatewayResponse sendNotification(NotificationGatewayRequest notificationGatewayRequest)  {
        ResponseEntity<NotificationGatewayResponse> response
                = restTemplate.postForEntity(notificationGatewayUrl, notificationGatewayRequest, NotificationGatewayResponse.class);
        return response.getBody();
    }

    public NotificationGatewayResponse bulkheadFallback(NotificationGatewayRequest request, Throwable throwable) {
        NotificationGatewayResponse response = new NotificationGatewayResponse();
        response.setStatus("WARNING");
        response.setStatusDescription("Bulk Head for Notification Gateway Service");
        return response;
    }

    public NotificationGatewayResponse circuitBreakerFallback(NotificationGatewayRequest request, Throwable throwable) {
        NotificationGatewayResponse response = new NotificationGatewayResponse();
        response.setStatus("WARNING");
        response.setStatusDescription("Circuit Breaker Opened for Notification Gateway Service");
        return response;
    }
}
