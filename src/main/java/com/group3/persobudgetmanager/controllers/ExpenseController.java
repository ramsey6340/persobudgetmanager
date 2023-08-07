package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.Expense;
import com.group3.persobudgetmanager.services.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/")
public class ExpenseController {
    @Autowired
    ExpenseService expenseService;
    @GetMapping("Expense")
    public List<Expense> findAll(){
        return expenseService.findAll();
    }
    @PostMapping("Expense/save")
    public Expense save(@Valid @RequestBody Expense expense){
        return expenseService.save(expense);
    }
/*
    @GetMapping("Expense/montant")
    public List<Expense> findAllByAmount(double montant){
        return expenseService.findAllByAmount(montant);
    }
    @GetMapping("Expense/creationDate")
    public List<Expense> findAllByCreationDate(LocalDate date){
        return expenseService.findAllByCreationDate(date);
    }
    @GetMapping("Expense/userId")
    public List<Expense> findAllByUserId(Long id){
        return expenseService.findAllByUserId(id);
    }
    @GetMapping("Expense/budgetId")
    List<Expense> findAllByPeriodIdAndUserIdAndBudgetId(Long id, Long userId, Long budgetId){
        return expenseService.findAllByPeriodIdAndUserIdAndBudgetId(id, userId, budgetId);
    }
    @GetMapping("Expense/periodIdAndBudgetId")
    public List<Expense> findAllByPeriodIdAndBudgetId(Long id, Long budgetId){
        return expenseService.findAllByPeriodIdAndBudgetId(id, budgetId);
    }
    @PostMapping("Expense/update")
    public Expense update(@RequestBody Expense expense){
        boolean b = expenseService.existsById(expense.getId());
       if (b==true){
           return expenseService.save(expense);
       }else{
           return new Expense();
       }
    }
*/
}
