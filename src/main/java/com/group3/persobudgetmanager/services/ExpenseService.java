package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.models.Budget;
import com.group3.persobudgetmanager.models.Expense;
import com.group3.persobudgetmanager.models.Period;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.repositories.BudgetRepository;
import com.group3.persobudgetmanager.repositories.ExpenseRepository;
import com.group3.persobudgetmanager.repositories.PeriodRepository;
import com.group3.persobudgetmanager.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private PeriodRepository periodRepository;
    public boolean existsById(Long id) {
        return expenseRepository.existsById(id);
    }

    //la méthode du service pour ajouter une nouvelle dépense
    public Expense save(Expense expense, Long userId, Long periodId, Long budgetId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Budget> budget = budgetRepository.findById(budgetId);
        Optional<Period> period = periodRepository.findById(periodId);
        if(user.isPresent() && budget.isPresent() && period.isPresent()){
            expense.setUser(user.get());
            expense.setBudget(budget.get());
            expense.setPeriod(period.get());
            return expenseRepository.save(expense);
        }else{
            return new Expense();
        }
    }
    //la méthode du service pour supprimer une dépense
    public ResponseEntity<String> delete(Long userId, Long expenseId){
        Optional<Expense> expenseOptional=expenseRepository.findByIdAndUserId(userId,expenseId);
        if (expenseOptional.isPresent()){
            expenseRepository.delete(expenseOptional.get());
            return ResponseEntity.ok("Supprission réussi!");
        }else{
            return new ResponseEntity<>("Objet non rétrouver!", HttpStatus.NOT_FOUND);
        }
    }

    //la méthode du service pour chercher une dépense
    public ResponseEntity<Expense> findById(Long userId, Long id){
        Optional<Expense> Expense1 = expenseRepository.findByIdAndUserId(id, userId);
        if (Expense1.isPresent()){
            return ResponseEntity.ok(Expense1.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    // la méthode retournant la liste des dépenses
    public List<Expense> findAll(){
      return expenseRepository.findAll();
    }
    public List<Expense> findAllByAmount(double montant){
        return expenseRepository.findAllByAmount(montant);
    }
    public List<Expense> findAllByCreationDate(LocalDate date){
        return expenseRepository.findAllByCreationDate(date);
    }
    public List<Expense> findAllByUserId(Long id){
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
    public List<Expense> findAllByPeriodIdAndUserIdAndBudgetId(Long id, Long userId, Long budgetId){
        return expenseRepository.findAllByPeriodIdAndUserIdAndBudgetId(id, userId, budgetId);
    }
    public List<Expense> findAllByPeriodIdAndBudgetId(Long id, Long budgetId){
        return expenseRepository.findAllByPeriodIdAndBudgetId(id, budgetId);
    }
    public Optional<Expense> findByIdAndUserIdAndBudgetId(Long id, Long userId, Long budgetId){
        return expenseRepository.findByIdAndUserIdAndBudgetId(id, userId, budgetId);
    }
    public Optional<Expense> findByIdAndUserId(Long id, Long userId){
        return expenseRepository.findByIdAndUserId(id, userId);
    }
    public Optional<Expense> findByIdAndUserIdAndPeriodId(Long id, Long userId, Long periodId){
        return expenseRepository.findByIdAndUserIdAndPeriodId(id, userId, periodId);
    }
    public Optional<Expense> findByIdAndUserIdAndBudgetIdAndPeriodId(Long id, Long userId, Long budgetId, Long periodId){
        return expenseRepository.findByIdAndUserIdAndBudgetIdAndPeriodId(id, userId, budgetId, periodId);
    }


    public ResponseEntity<Expense> update(Long userId, Long expenseId, Expense expense) {
        Optional<Expense> expense1 =expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expense1.isPresent()){
            expense1.get().setAmount(expense.getAmount());
            expense1.get().setPeriod(expense.getPeriod());
            expense1.get().setCreationDate(expense.getCreationDate());
            expense1.get().setNote(expense.getNote());
            return ResponseEntity.ok(expenseRepository.save(expense1.get()));
        }else{
            return ResponseEntity.notFound().build();
        }

        }

    public ResponseEntity<Expense> update2(Long userId, Long expenseId, Map<String, Object> expenseMap) {
        Optional<Expense> expenseOptional = expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expenseOptional.isPresent()){
            if (expenseMap.containsKey("amount")){
                expenseOptional.get().setAmount((Double) expenseMap.get("amount"));
            }
            if (expenseMap.containsKey("note")){
                expenseOptional.get().setNote((String) expenseMap.get("note"));
            }
            return ResponseEntity.ok(expenseRepository.save(expenseOptional.get()));
        }else{
            return ResponseEntity.notFound().build();
        }
        }
        public List<Expense> search(Long userId, Double amount, String note){
         return expenseRepository.findByUserIdOrAmountOrNoteContaining(userId,amount,note);
        }

    }

