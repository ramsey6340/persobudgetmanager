package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.models.Period;
import com.group3.persobudgetmanager.repositories.PeriodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeriodService {
    @Autowired
    private PeriodRepository periodRepository;
    public PeriodService(PeriodRepository periodRepository){
        this.periodRepository = periodRepository;
    }
    // La méthode retournant une période correspondant à un utilisateur
    Optional<Period> findByIdAndUserId(Long id, Long userId){
        return periodRepository.findByIdAndUserId(id, userId);
    }
    // La méthoide retournant une liste des périodes correspondant à un utilisateur selon la description
    List<Period> findAllByDescriptionContaining(String keyword){
        return periodRepository.findAllByDescriptionContaining(keyword);
    }
    // La méthode retournant une liste des périodes propre à un utilisateur
    List<Period> findAllByIdAndUserId(Long id, Long userId){
        return periodRepository.findAllByIdAndUserId(id, userId);
    }
    // La méthode retournant les périodes selon le nombre de jours
    List<Period> findAllByNbJour(Long nbJour){
        return periodRepository.findAllByNbJour(nbJour);
    }
}
