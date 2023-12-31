package com.group3.persobudgetmanager.projections;

import java.time.LocalDate;

// Elle est utilisé pour la mise en forme des données des dépenses
public interface ExpenseProjection {
    Long getId();
    Double getAmount();
    String getDescription();
    LocalDate getCreationDate();
    String getUserId();
    Long getBudgetId();
    Long getPeriodId();
}
