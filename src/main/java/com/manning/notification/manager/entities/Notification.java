package com.manning.notification.manager.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name="NOTIFICATION")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String customerId;
    private String notificationMode;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<NotificationParameter> notificationParameters;
    private String notificationTemplateName;

}
