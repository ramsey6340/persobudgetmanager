package com.group3.persobudgetmanager.projections;
import java.time.LocalDate;

// Elle est utilisé pour la mise en forme des données du Budget
public interface BudgetProjection {
    Long getId();
    Double getAmount();
    String getTitle();
    Double getRemainder();
    LocalDate getCreationDate();
    LocalDate getStartDate();
    LocalDate getEndDate();
    Long getUserId();
    Long getCategoryId();
}
