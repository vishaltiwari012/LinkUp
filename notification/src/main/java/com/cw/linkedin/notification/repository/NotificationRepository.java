package com.cw.linkedin.notification.repository;

import com.cw.linkedin.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
