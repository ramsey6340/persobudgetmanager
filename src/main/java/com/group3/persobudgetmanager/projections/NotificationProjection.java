package com.group3.persobudgetmanager.projections;

public interface NotificationProjection {
    Long getId();
    String getContent();
    String getUserFullName();
    Double getBudgetRemainder();
}
