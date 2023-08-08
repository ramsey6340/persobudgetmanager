package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Budget;
import com.group3.persobudgetmanager.projections.BudgetProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT b.id AS id, b.amount AS amount, b.alertAmount AS alertAmount, b.title AS title," +
            " b.remainder AS remainder, b.creationDate AS creationDate, u.fullName AS userFullName," +
            " c.title AS categoryTitle FROM Budget b JOIN b.user u JOIN b.category c " +
            "WHERE b.user.id=:userId AND b.delete=false")
    List<BudgetProjection> findAllBudgetsWithUser(@Param("userId") Long userId);

    @Query("SELECT b.id AS id, b.amount AS amount, b.alertAmount AS alertAmount, b.title AS title," +
            " b.remainder AS remainder, b.creationDate AS creationDate, u.fullName AS userFullName," +
            " c.title AS categoryTitle FROM Budget b JOIN b.user u JOIN b.category c " +
            "WHERE b.user.id=:userId AND b.id=:budgetId AND b.delete=false")
    Optional<BudgetProjection> findBudgetWithIdAndUser(@Param("budgetId") Long budgetId, @Param("userId") Long userId);
}