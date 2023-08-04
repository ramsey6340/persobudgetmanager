package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {
    // La méthode retournant une période correspondant à un utilisateur
    Optional<Period> findByIdAndUserId(Long id, Long userId);
    // La méthoide retournant une liste des périodes correspondant à un utilisateur selon la description
    List<Period> findAllByDescriptionContaining(String keyword);
    // La méthode retournant une liste des périodes propre à un utilisateur
    List<Period> findAllByIdAndUserId(Long id, Long userId);
    // La méthode retournant les périodes selon le nombre de jours
    List<Period> findAllByNbJour(Long nbJour);

}
