package com.manning.notification.manager.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class NotificationParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String notificationParameterName;
    private String notificationParameterValue;
}
