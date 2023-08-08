package com.group3.persobudgetmanager.projections;

import java.time.LocalDate;

public interface ExpenseProjection {
    Long getId();
    Double getAmount();
    String getDescription();
    LocalDate getCreationDate();
    LocalDate getStartDate();
    LocalDate getEndDate();
    String getUserFullName();
    Double getBudgetRemainder();
    String getPeriodTitle();
}
