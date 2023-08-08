package com.group3.persobudgetmanager.projections;

public interface PeriodProjection {
    Long getId();
    String getTitle();
    String getDescription();
    int getNbDay();
    String getUserFullName();
}
