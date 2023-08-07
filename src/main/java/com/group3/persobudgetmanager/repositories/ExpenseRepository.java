package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Expense;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    // La méthode retournant la liste des dépenses correspondant à un montant
    List<Expense> findAllByAmount(double montant);
    // La méthode retournant la liste des depenses correspondant à une date
    List<Expense> findAllByCreationDate(LocalDate date);
    // La méthode retournant la liste des depenses correspondant à un utilisateur
    List<Expense> findAllByUserId(Long id);
    // La méthode retournant la liste des depenses correspondant à un budget
    List<Expense> findAllByBudgetId(Long id);
    // La méthode retournant la liste des depenses correspondant à un période
    List<Expense> findAllByPeriodId(Long id);
    // La méthode retournant la liste des depenses correspondant à un utilisateur et une période
    List<Expense> findAllByPeriodIdAndUserId(Long id, Long userId);
    // La méthode retournant la liste des depenses correspondant à un utilisateur et un budget
    List<Expense> findAllByUserIdAndBudgetId(Long userId, Long budgetId);
    // La méthode retournant la liste des depenses correspondant à un budget et un période
    List<Expense> findAllByPeriodIdAndUserIdAndBudgetId(Long id, Long userId, Long budgetId);
    // La méthode retournant la liste des depenses correspondant à un utilisateur, un budget et un période
    List<Expense> findAllByPeriodIdAndBudgetId(Long id, Long budgetId);
    // La méthode retournant la liste des depenses correspondant à un utilisateur, un budget et un période
    Optional<Expense> findByIdAndUserIdAndBudgetId(Long id, Long userId, Long budgetId);
    // La méthode retournant la liste des depenses correspondant à un utilisateur, un budget et un période
    Optional<Expense> findByIdAndUserId(Long id, Long userId);
    // La méthode retournant la liste des depenses correspondant à un utilisateur, un budget et un période
    Optional<Expense> findByIdAndUserIdAndPeriodId(Long id, Long userId, Long periodId);
    // La méthode retournant la liste des depenses correspondant à un utilisateur, un budget et un période
    Optional<Expense> findByIdAndUserIdAndBudgetIdAndPeriodId(Long id, Long userId, Long budgetId, Long periodId);

    void deleteByIdAndUserId(Long expenseId, Long userId);
    List<Expense> findByUserIdOrAmountOrNoteContaining(Long userId, Double amount, String note);
}