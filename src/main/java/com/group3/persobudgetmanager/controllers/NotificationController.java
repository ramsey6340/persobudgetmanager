package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.Notification;
import com.group3.persobudgetmanager.projections.NotificationProjection;
import com.group3.persobudgetmanager.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @Operation(summary = "Création d'une nouvelle notification pour un utilisateur et un budget donné")
    @PostMapping("users/{userId}/budgets/{budgetId}/notifications")
    public ResponseEntity<Object> create(@PathVariable Long userId, @PathVariable Long budgetId, @Valid @RequestBody Notification notification) {
        return notificationService.create(userId, budgetId, notification);
    }

    @Operation(summary = "Recuperer la liste des notifications")
    @GetMapping("users/{userId}/budgets/{budgetId}/notifications")
    public List<Notification> getNotificationsForUserAndBudget(@PathVariable Long userId, @PathVariable Long budgetId) {
        return notificationService.getNotificationsForUserAndBudget(userId, budgetId);
    }

    @Operation(summary = "Récuperer une notification d'un budget pour un utilisateur")
    @GetMapping("users/{userId}/budgets/{budgetId}/notifications/{notificationId}")
    public ResponseEntity<Object> getNotificationForUserAndBudget(@PathVariable Long userId, @PathVariable Long budgetId, @PathVariable Long notificationId) {
        return notificationService.getNotification(userId, budgetId, notificationId);
    }

    @Operation(summary = "Récuperer les notifications d'un utilisateur")
    @GetMapping("users/{userId}/notifications")
    public List<NotificationProjection> getNotificationsForUser(@PathVariable Long userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    @Operation(summary = "Récuperer une notification d'un utilisateur")
    @GetMapping("users/{userId}/notifications/{notificationId}")
    public ResponseEntity<Object> getNotificationForUser(@PathVariable Long userId, @PathVariable Long notificationId) {
        return notificationService.getNotificationForUser(userId, notificationId);
    }

    @Operation(summary = "Suppression d'une notification")
    @DeleteMapping("users/{userId}/notifications/{notificationId}")
    public ResponseEntity<Object> deleteNotification(@PathVariable Long userId, @PathVariable Long notificationId) {
        return notificationService.deleteNotification(userId, notificationId);
    }

    @Operation(summary = "Rechercher des notifications par leur contenue")
    @GetMapping(value = "notifications", params = "content")
    public List<Notification> getNotificationsByContentContaining(@RequestParam("content") String contentKeyWord) {
        return notificationService.getNotificationsByContentContaining(contentKeyWord);
    }

    @Operation(summary = "Rechercher des notifications par le Budget")
    @GetMapping(value = "notifications", params = "budget")
    public List<Notification> getNotificationsByBudget(@RequestParam("budget") Long budgetId) {
        return notificationService.getNotificationsByBudget(budgetId);
    }

    @Operation(summary = "Rechercher des notifications par l'utilisateur")
    @GetMapping(value = "notifications", params = "user")
    public List<Notification> getNotificationsByUser(@RequestParam("user") Long userId) {
        return notificationService.getNotificationsByUser(userId);
    }

}
