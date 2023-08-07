package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.Expense;
import com.group3.persobudgetmanager.services.ExpenseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
public class ExpenseController {
    @Autowired
    ExpenseService expenseService;
    @Operation(summary = "Obtenir la liste des dépenses")
    @GetMapping("Expense")
    public List<Expense> findAll(){
        return expenseService.findAll();
    }
    @Operation(summary = "Ajouter une nouvelle dépense")
    @PostMapping(value = "users/{userId}/expenses", params = {"period", "budget"})
    public Expense save(@Valid @RequestBody Expense expense, @PathVariable Long userId, @RequestParam("period") Long period, @RequestParam("budget") Long budget){
        return expenseService.save(expense, userId, period, budget);
    }

    @Operation(summary = "Afficher toutes les dépense d'un utilisateur")
    @GetMapping("users/{userId}/expenses")
    public List<Expense> findAllByUserId(@PathVariable Long userId){
        return expenseService.findAllByUserId(userId);
    }
    @Operation(summary = "Afficher une dépense d'un budget")
    @GetMapping("users/{userId}/expenses/{expenseId}")
    public ResponseEntity<Expense> findById(@PathVariable Long userId, @PathVariable Long expenseId){
        return expenseService.findById(userId, expenseId);
    }
    @Operation(summary = "Modifier une dépense")
    @PutMapping(value = "users/{userId}/expenses/{expenseId}")
    public ResponseEntity<Expense> update(@PathVariable Long userId, @PathVariable Long expenseId, @RequestBody Expense expense){
        return expenseService.update(userId, expenseId, expense);
    }
    @Operation(summary = "mis à jour partiel d'une dépense")
    @RequestMapping(value = "api/users/{userId}/expenses/{expenseId}", method = RequestMethod.PATCH)
    public ResponseEntity<Expense> update2(@PathVariable Long userId, @PathVariable Long expenseId, @RequestBody Map<String, Object> expenseMap){
        return expenseService.update2(userId, expenseId, expenseMap);
    }
    @DeleteMapping(value = "users/{userId}/expenses/{expenseId}")
    public ResponseEntity<String> delete(@PathVariable Long userId, @PathVariable Long expenseId){
        return expenseService.delete(expenseId, userId);
    }
    @GetMapping(value = "users/{userId}/expenses", params={"amount", "note"})
    public List<Expense> search(@PathVariable Long userId,@RequestParam("amount") Double amount, @RequestParam("note") String note){
        return expenseService.search(userId,amount,note);
    }
}

