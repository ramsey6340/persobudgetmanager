package com.group3.persobudgetmanager.projections;

import java.time.LocalDate;

public interface BudgetProjection {
    Long getId();
    Double getAmount();
    String getTitle();
    Double getRemainder();
    LocalDate getCreationDate();
    String getUserFullName();
    String getCategoryTitle();
}
