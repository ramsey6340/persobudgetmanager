package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.exceptions.CustomException;
import com.group3.persobudgetmanager.exceptions.ErrorMessage;
import com.group3.persobudgetmanager.exceptions.NotFoundException;
import com.group3.persobudgetmanager.models.Budget;
import com.group3.persobudgetmanager.models.Notification;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.repositories.BudgetRepository;
import com.group3.persobudgetmanager.repositories.NotificationRepository;
import com.group3.persobudgetmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    // Création d'une nouvelle notification
    public ResponseEntity<Object> create(Long userId, Long budgetId, Notification notification) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Budget> budgetOptional = budgetRepository.findById(budgetId);

        if (userOptional.isPresent() && budgetOptional.isPresent()) {
            notification.setBudget(budgetOptional.get());
            notification.setUser(userOptional.get());
            notificationRepository.save(notification);
            URI location = ServletUriComponentsBuilder.
                    fromCurrentRequest().
                    path("{id}").
                    buildAndExpand(notification.getId()).
                    toUri();

            return ResponseEntity.created(location).body(notification);
        }
        else
            throw new NotFoundException(ErrorMessage.notFound);    }

    // Recuperer toutes les notifications d'un utilisateur pour un budget
    public List<Notification> getNotificationsForUserAndBudget(Long userId, Long budgetId) {
        return notificationRepository.findAllByUserIdAndBudgetIdAndDeleteFalse(userId, budgetId);
    }

    public ResponseEntity<Object> getNotification(Long userId, Long budgetId, Long notificationId) {
        Optional<Notification> notificationOptional= notificationRepository.findByIdAndUserIdAndBudgetIdAndDeleteFalse(notificationId, userId, budgetId);
        if (notificationOptional.isPresent()){
            return new ResponseEntity<>(notificationOptional.get(), HttpStatus.OK);
        }
        else {
            throw new NotFoundException(ErrorMessage.notFound);        }
    }

    public List<Notification> getNotificationsForUser(Long userId) {
        //return notificationRepository.findAllByUserIdAndDeleteFalse(userId);
        return notificationRepository.findAllNotificationsByUser(userId);
    }

    public ResponseEntity<Object> getNotificationForUser(Long userId, Long notificationId) {
        //Optional<Notification> notificationOptional = notificationRepository.findByIdAndUserIdAndDeleteFalse(notificationId, userId);
        Optional<Notification> notificationOptional = notificationRepository.findNotificationByIdAndUser(notificationId, userId);
        if (notificationOptional.isPresent()) {
            return new ResponseEntity<>(notificationOptional.get(), HttpStatus.OK);
        }
        else {
            throw new NotFoundException(ErrorMessage.notFound);        }
    }

    public ResponseEntity<Object> deleteNotification(Long userId, Long notificationId) {
        Optional<Notification> notificationOptional = notificationRepository.findByIdAndUserIdAndDeleteFalse(notificationId, userId);
        if(notificationOptional.isPresent()) {
            notificationOptional.get().setDelete(true);
            notificationRepository.save(notificationOptional.get());
            return new ResponseEntity<>("Suppression reussi", HttpStatus.OK);
        }
        else
            throw new NotFoundException(ErrorMessage.notFound);    }

    public List<Notification> getNotificationsByContentContaining(String contentKeyWord) {
        return notificationRepository.findAllByContentContainingAndDeleteFalse(contentKeyWord);
    }

    public List<Notification> getNotificationsByBudget(Long budgetId) {
        return notificationRepository.findAllByBudgetIdAndDeleteFalse(budgetId);
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findAllByUserIdAndDeleteFalse(userId);
    }
}