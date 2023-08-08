package com.group3.persobudgetmanager.projections;

// Elle est utilisé pour la mise en forme des données des Notifications
public interface NotificationProjection {
    Long getId();
    String getContent();
    Long getUserId();
    Long getBudgetId();
}
