package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.Budget;
import com.group3.persobudgetmanager.projections.BudgetProjection;
import com.group3.persobudgetmanager.services.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Map;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/api")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;
    @Operation(summary = "Création de budget pour un utilisateur")
    @PostMapping(value = "users/{userId}/budgets", params = "category")
    public ResponseEntity<Object> createBudget(@Valid @RequestBody Budget budget, @PathVariable Long userId, @RequestParam("category") Long categoryId){
        return budgetService.createBudget(budget, userId, categoryId);
    }
    @Operation(summary = "Récuperer tous les budget d'un utilisateur")
    @GetMapping("users/{userId}/budgets")
    public List<BudgetProjection> getAllBudgets(@PathVariable Long userId){
        return budgetService.getAllBudgets(userId);

    }
    @Operation(summary = "Récuperer un  budget spécifique pour un utilisateur")
    @GetMapping("users/{userId}/budgets/{budgetId}")
    public Optional<BudgetProjection>  getBudgetById(@PathVariable Long userId, @PathVariable Long budgetId){
        return budgetService.getBudgetByIdAndUserId(userId, budgetId);
    }

    @Operation(summary = "Modifier un Budget")
    @PutMapping("users/{userId}/budgets/{budgetId}")
    public Object updateBudget(@PathVariable Long userId, @PathVariable Long budgetId,@RequestBody Budget budget){
     return budgetService.updateBudget(userId,budgetId,budget);
    }

    @Operation(summary = "Modifier un ou plus élément du budget")
    @RequestMapping(value = "/users/{userId}/budgets/{budgetId}",method = RequestMethod.PATCH)
    public Object patchBudget(@PathVariable Long userId, @PathVariable Long budgetId, @RequestBody Map<String,Object> budget){
        return budgetService.patchBudget(userId,budgetId,budget);
    }

    @Operation(summary = "Supprimer le budget d'un utilisateur")
    @DeleteMapping("users/{userId}/budgets/{budgetId}")
    public Object deleteBudget(@PathVariable Long userId,@PathVariable Long budgetId, Budget budget){
        return budgetService.deleteBudget(userId,budgetId,budget);
    }

    @Operation(summary = "Lister tous les budgets a travers un attribut")
    @GetMapping(value = "users/{userId}/budgets", params = {"amount","alertAmount", "title","category"})
    public List<Budget>  getBudgetByAttribut (@PathVariable Long userId, @RequestParam("amount") Double montant,@RequestParam("alertAmount")
    Double alertMontant, @RequestParam("title") String titre,@RequestParam("category") Long categorie ){
        return budgetService.getBudgetByAttribut(userId,montant,alertMontant,titre, categorie );
    }
}
