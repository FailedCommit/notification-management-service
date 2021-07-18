package com.manning.notification.manager.repositories;

import com.manning.notification.manager.entities.Notification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends PagingAndSortingRepository<Notification, String> {
    Notification findByCustomerId(String customerId);
}
