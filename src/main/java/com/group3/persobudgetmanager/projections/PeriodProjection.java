package com.group3.persobudgetmanager.projections;

// Elle est utilisé pour la mise en forme des données des Périodes
public interface PeriodProjection {
    Long getId();
    String getTitle();
    String getDescription();
    int getNbDay();
    String getUserFullName();
}
