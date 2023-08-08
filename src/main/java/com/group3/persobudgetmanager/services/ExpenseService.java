package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.models.Expense;
import com.group3.persobudgetmanager.repositories.ExpenseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ExpenseService {
    @Autowired
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }
    //la méthode du service pour ajouter une nouvelle dépense
    public Expense save(Expense expense){
        return expenseRepository.save(expense);
    }
    //la méthode du service pour supprimer une dépense
    public void delete(Long id){
        expenseRepository.deleteById(id);
    }
  //la méthode du service pour modifier une dépense
    public Expense update(Expense expense){
        return expenseRepository.save(expense);
    }
    //la méthode du service pour chercher une dépense
    public Optional<Expense> findById(Long id){
        return expenseRepository.findById(id);
    }
    // la méthode retournant la liste des dépenses
    public List<Expense> findAll(){
      return expenseRepository.findAll();
  }
    List<Expense> findAllByAmount(double montant){
        return expenseRepository.findAllByAmount(montant);
    }
    List<Expense> findAllByCreationDate(LocalDate date){
        return expenseRepository.findAllByCreationDate(date);
    }
    List<Expense> findAllByUserId(Long id){
        return expenseRepository.findAllByUserId(id);
    }
    List<Expense> findAllByBudgetId(Long id){
        return expenseRepository.findAllByBudgetId(id);
    }
    List<Expense> findAllByPeriodId(Long id){
        return expenseRepository.findAllByPeriodId(id);
    }
    List<Expense> findAllByPeriodIdAndUserId(Long id, Long userId){
        return expenseRepository.findAllByPeriodIdAndUserId(id, userId);
    }
    List<Expense> findAllByUserIdAndBudgetId(Long userId, Long budgetId){
        return expenseRepository.findAllByUserIdAndBudgetId(userId, budgetId);
    }
    List<Expense> findAllByPeriodIdAndUserIdAndBudgetId(Long id, Long userId, Long budgetId){
        return expenseRepository.findAllByPeriodIdAndUserIdAndBudgetId(id, userId, budgetId);
    }
    List<Expense> findAllByPeriodIdAndBudgetId(Long id, Long budgetId){
        return expenseRepository.findAllByPeriodIdAndBudgetId(id, budgetId);
    }
    Optional<Expense> findByIdAndUserIdAndBudgetId(Long id, Long userId, Long budgetId){
        return expenseRepository.findByIdAndUserIdAndBudgetId(id, userId, budgetId);
    }
    Optional<Expense> findByIdAndUserId(Long id, Long userId){
        return expenseRepository.findByIdAndUserId(id, userId);
    }
    Optional<Expense> findByIdAndUserIdAndPeriodId(Long id, Long userId, Long periodId){
        return expenseRepository.findByIdAndUserIdAndPeriodId(id, userId, periodId);
    }
    Optional<Expense> findByIdAndUserIdAndBudgetIdAndPeriodId(Long id, Long userId, Long budgetId, Long periodId){
        return expenseRepository.findByIdAndUserIdAndBudgetIdAndPeriodId(id, userId, budgetId, periodId);
    }



}
