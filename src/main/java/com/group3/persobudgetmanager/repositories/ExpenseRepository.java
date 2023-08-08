package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findAllByUserId(Long id);
    // La méthode retournant la liste des depenses correspondant à un utilisateur, un budget et un période
    Optional<Expense> findByIdAndUserId(Long id, Long userId);
    List<Expense> findByUserIdOrAmountOrDescriptionContaining(Long userId, Double amount, String note);
}