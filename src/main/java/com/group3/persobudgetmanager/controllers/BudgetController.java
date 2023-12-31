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

    @Operation(summary = "Affichage de tous les budget supprimes d'un utilisateur")
    @GetMapping("users/{userId}/budgets/trash")
    public List<BudgetProjection> getAllBudgetsTrash(@PathVariable Long userId){
        return budgetService.getAllBudgetsTrash(userId);

    }
    @Operation(summary = "Récuperer un  budget spécifique pour un utilisateur")
    @GetMapping("users/{userId}/budgets/{budgetId}")
    public Optional<BudgetProjection> getBudgetById(@PathVariable Long userId, @PathVariable Long budgetId){
        return budgetService.getBudgetByIdAndUserId(userId, budgetId);
    }

    @Operation(summary = "Transferer le reliquat d'un budget à un autre budget")
    @GetMapping("users/{userId}/budgets/transfer/{budgetId1}/to/{budgetId2}")
    public ResponseEntity<Object> transferBudget(@PathVariable Long userId, @PathVariable Long budgetId1, @PathVariable Long budgetId2){
        return budgetService.transferBudget(userId, budgetId1, budgetId2);
    }

    @Operation(summary = "Modifier un Budget")
    @PutMapping(value = "users/{userId}/budgets/{budgetId}", params = "category")
    public Object updateBudget(@PathVariable Long userId, @PathVariable Long budgetId,@RequestBody Budget budget, @RequestParam("category") Long categoryId){
     return budgetService.updateBudget(userId,budgetId, categoryId, budget);
    }

    @Operation(summary = "Modifier un ou plus élément du budget")
    @RequestMapping(value = "/users/{userId}/budgets/{budgetId}", method = RequestMethod.PATCH)
    public Object patchBudget(@PathVariable Long userId, @PathVariable Long budgetId, @RequestBody Map<String,Object> budget){
        return budgetService.patchBudget(userId, budgetId, budget);
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
