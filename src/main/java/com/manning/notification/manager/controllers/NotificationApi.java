package com.manning.notification.manager.controllers;

import com.manning.notification.manager.model.NotificationGatewayRequest;
import com.manning.notification.manager.model.NotificationGatewayResponse;
import com.manning.notification.manager.model.NotificationRequest;
import com.manning.notification.manager.model.NotificationResponse;
import com.manning.notification.manager.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(("/api/notifications"))
@AllArgsConstructor
public class NotificationApi {
    private final NotificationService notificationService;

    @PostMapping
    public NotificationResponse sendNotification(@RequestBody NotificationRequest request) {
        return notificationService.sendNotification(request);
    }
}
