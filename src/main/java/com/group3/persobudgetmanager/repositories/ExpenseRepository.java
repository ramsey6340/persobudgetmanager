package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Expense;
import com.group3.persobudgetmanager.projections.ExpenseProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // La méthode retournant la liste des depenses correspondant à un utilisateur, un budget et un période
    Optional<Expense> findByIdAndUserId(Long id, Long userId);
    List<Expense> findByUserIdAndAmountOrDescriptionContainingOrPeriodTitleContainingOrBudgetId(Long userId, Double amount, String description, String periodTitle, Long budgetId);

    // récuperer la liste des dépenses correspondant à un utilisateur en utilisant une projection
    @Query("SELECT e.id AS id, e.amount AS amount, e.description AS description, e.creationDate AS creationDate, " +
            "u.id AS userId , b.id AS budgetId, p.id AS periodId " +
            "FROM Expense e JOIN e.user u JOIN e.budget b JOIN e.period p WHERE e.user.id=:userId AND e.delete=false")
    List<ExpenseProjection> findAllExpensesWithUser(@Param("userId") Long userId);

    @Query("SELECT e.id AS id, e.amount AS amount, e.description AS description, e.creationDate AS creationDate, " +
            "u.id AS userId , b.id AS budgetId, p.id AS periodId " +
            "FROM Expense e JOIN e.user u JOIN e.budget b JOIN e.period p WHERE e.user.id=:userId AND e.delete=true")
    List<ExpenseProjection> findAllExpensesWithUserTrash(@Param("userId") Long userId);

    @Query("SELECT e.id AS id, e.amount AS amount, e.description AS description, e.creationDate AS creationDate, " +
            "u.id AS userId , b.id AS budgetId, p.id AS periodId " +
            "FROM Expense e JOIN e.user u JOIN e.budget b JOIN e.period p WHERE e.user.id=:userId AND e.id=:expenseId AND e.delete=false")
    Optional<ExpenseProjection> findExpenseWithIdAndUserId(@Param("userId") Long userId, @Param("expenseId") Long expenseId);
}