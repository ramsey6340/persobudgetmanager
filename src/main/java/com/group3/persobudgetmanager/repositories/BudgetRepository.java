package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    List<Budget> findAllByUserId(Long userId);


    

    Budget findBudgetById(Long budgetId);

    Optional<Budget> findByUserIdAndId(Long userId, Long budgetId);


    void deleteByUserIdAndId(Long userId, Long budgetId);

    List<Budget> findByUserIdAndAmountOrAlertAmountOrTitleOrCategoryId(Long userId, Double montant, Double alertMontant, String titre, Long categorie);
}