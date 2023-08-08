package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByContentContainingAndDeleteFalse(String keyword);
    List<Notification> findAllByUserIdAndDeleteFalse(Long userId);
    List<Notification> findAllByBudgetIdAndDeleteFalse(Long budgetId);
    List<Notification> findAllByUserIdAndBudgetIdAndDeleteFalse(Long userId, Long budgetId);
    Optional<Notification> findByIdAndUserIdAndBudgetIdAndDeleteFalse(Long notificationId, Long userId, Long budgetId);
    Optional<Notification> findByIdAndUserIdAndDeleteFalse(Long notificationId, Long userId);
}