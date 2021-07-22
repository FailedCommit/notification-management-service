package com.manning.notification.manager.services;

import com.manning.notification.manager.model.NotificationGatewayRequest;
import com.manning.notification.manager.model.NotificationGatewayResponse;
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

    @CircuitBreaker(name = "circuitBreakerService", fallbackMethod = "circuitBreakerFallback")
    public NotificationGatewayResponse sendNotification(NotificationGatewayRequest notificationGatewayRequest)  {
        ResponseEntity<NotificationGatewayResponse> response
                = restTemplate.postForEntity(notificationGatewayUrl, notificationGatewayRequest, NotificationGatewayResponse.class);
        return response.getBody();
    }

    public NotificationGatewayResponse circuitBreakerFallback(NotificationGatewayRequest request, Throwable throwable) {
        NotificationGatewayResponse response = new NotificationGatewayResponse();
        response.setStatus("WARNING");
        response.setStatusDescription("Circuit Breaker Opened for Notification Gateway Service");
        return response;
    }
}
