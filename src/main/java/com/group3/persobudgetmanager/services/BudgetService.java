package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.models.Budget;
import com.group3.persobudgetmanager.models.Category;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.repositories.BudgetRepository;
import com.group3.persobudgetmanager.repositories.CategoryRepository;
import com.group3.persobudgetmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private BudgetRepository budgetRepository;
    public ResponseEntity<String> createBudget(Budget budget, Long userId, Long categoryId) {
        Optional<User> user=userRepository.findById(userId);
        Optional<Category> category= categoryRepository.findById(categoryId);

        if (user.isPresent() && category.isPresent()){
            budget.setUser(user.get());

            budget.setCategory(category.get());
            budgetRepository.save(budget) ;
            return ResponseEntity.ok("budget enregistré avec succès");
        }
        return ResponseEntity.notFound().build();
    }

    public List<Budget> getAllBudgets(Long userId) {
        return budgetRepository.findAllByUserId(userId);
    }


    public Optional<Budget> getBudgetByIdAndUserId(Long userId, Long budgetId) {
        return budgetRepository.findByUserIdAndId(userId, budgetId);
    }

    public Object updateBudget(Long userId, Long budgetId,  Budget budget) {

                Optional<Budget> modifBudget = budgetRepository.findByUserIdAndId(userId,budgetId);
        if(modifBudget.isPresent()){

            modifBudget.get().setAmount(budget.getAmount());
            modifBudget.get().setAlertAmount(budget.getAlertAmount());
            modifBudget.get().setTitle(budget.getTitle());
            modifBudget.get().setCategory(budget.getCategory());
            return budgetRepository.save(modifBudget.get());
    }else {
         return "Ce Budget n'existe pas";
        }
    }

    public Object patchBudget(Long userId, Long budgetId, Map<String,Object> budgetMap) {
        Optional<Budget> patchingBudget = budgetRepository.findByUserIdAndId(userId, budgetId);
        if (patchingBudget.isPresent()) {
            if (budgetMap.containsKey("amount")) {
                patchingBudget.get().setAmount((Double) budgetMap.get("amount"));
            }

                if (budgetMap.containsKey("alertAmount")) {
                    patchingBudget.get().setAlertAmount((Long) budgetMap.get("alertAmount"));
                }
                if (budgetMap.containsKey("title")) {
                    patchingBudget.get().setTitle((String) budgetMap.get("title"));
                }
                if (budgetMap.containsKey("category")) {
                    patchingBudget.get().setCategory((Category) budgetMap.get("category"));

                }
            return budgetRepository.save(patchingBudget.get());
            }else {
            return "Ce Budget n'a pas été trouvé";
        }

        }

    public Object deleteBudget(Long userId, Long budgetId, Budget budget) {
        Optional<Budget> supprimBudget = budgetRepository.findByUserIdAndId(userId,budgetId);
        if (supprimBudget.isPresent()){
            budgetRepository.deleteByUserIdAndId(userId,budgetId);
            return "Budget Supprimé avec succès";
        }else {
            return "Ce Budget n'a pas été trouvé";
        }
    }

    public List<Budget> getBudgetByAttribut(Long userId, Double montant, Double alertMontant, String titre, Long categorie) {
        return budgetRepository.findByUserIdAndAmountOrAlertAmountOrTitleOrCategoryId
                (userId,montant, alertMontant, titre,categorie);
    }
}

