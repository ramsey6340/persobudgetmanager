package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.Expense;
import com.group3.persobudgetmanager.projections.ExpenseProjection;
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

    @Operation(summary = "Ajouter une nouvelle dépense")
    @PostMapping(value = "users/{userId}/expenses", params = {"period","budget"})
    public ResponseEntity<Object> save(@Valid @RequestBody Expense expense, @PathVariable Long userId, @RequestParam("period") Long period, @RequestParam("budget") Long budget){
        return expenseService.save(expense, userId, period, budget);
    }

    @Operation(summary = "Afficher toutes les dépense d'un utilisateur")
    @GetMapping("users/{userId}/expenses")
    public List<ExpenseProjection> findAllByUserId(@PathVariable Long userId){
        return expenseService.findAllByUserId(userId);
    }

    @Operation(summary = "Afficher toutes les dépense supprimer d'un utilisateur")
    @GetMapping("users/{userId}/expenses/trash")
    public List<ExpenseProjection> findAllByUserIdTrash(@PathVariable Long userId){
        return expenseService.findAllByUserIdTrash(userId);
    }
    @Operation(summary = "Afficher une dépense d'un budget")
    @GetMapping("users/{userId}/expenses/{expenseId}")
    public ResponseEntity<Object> findById(@PathVariable Long userId, @PathVariable Long expenseId){
        return expenseService.findById(userId, expenseId);
    }
    @Operation(summary = "Restauration d'une dépense supprimé")
    @GetMapping("users/{userId}/expenses/{expenseId}/restore")
    public ResponseEntity<Object> restoreExpense(@PathVariable Long userId, @PathVariable Long expenseId){
        return expenseService.restoreExpense(userId, expenseId);
    }
    @Operation(summary = "Modifier une dépense")
    @PutMapping(value = "users/{userId}/expenses/{expenseId}")
    public ResponseEntity<Object> update(@PathVariable Long userId, @PathVariable Long expenseId,@Valid @RequestBody Expense expense){
        return expenseService.update(userId, expenseId, expense);
    }
    @Operation(summary = "mis à jour partiel d'une dépense")
    @PatchMapping(value = "users/{userId}/expenses/{expenseId}")
    public ResponseEntity<Object> updatePatch(@PathVariable Long userId, @PathVariable Long expenseId,@Valid @RequestBody Map<String, Object> expenseMap){
        return expenseService.updatePatch(userId, expenseId, expenseMap);
    }
    @Operation(summary = "Supprimer une depense")
    @DeleteMapping(value = "users/{userId}/expenses/{expenseId}")
    public ResponseEntity<String> delete(@PathVariable Long userId, @PathVariable Long expenseId){
        return expenseService.delete(expenseId, userId);
    }
    @Operation(summary = "Faire une rechercher personnalisée")
    @GetMapping(value = "users/{userId}/expenses", params={"amount", "description", "period", "budget"})
    public List<Expense> search(@PathVariable Long userId, @RequestParam("amount") Double amount,
                                @RequestParam("description") String description,
                                @RequestParam("periodTitle") String periodTitle,
                                @RequestParam("budget") Long budget){
        return expenseService.search(userId,amount,description,periodTitle,budget);
    }

}

