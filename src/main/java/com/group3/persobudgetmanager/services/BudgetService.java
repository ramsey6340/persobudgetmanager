package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.models.Budget;
import com.group3.persobudgetmanager.models.Category;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.projections.BudgetProjection;
import com.group3.persobudgetmanager.repositories.BudgetRepository;
import com.group3.persobudgetmanager.repositories.CategoryRepository;
import com.group3.persobudgetmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BudgetRepository budgetRepository;

    public ResponseEntity<Object> createBudget(Budget budget, Long userId, Long categoryId) {
        Optional<User> user=userRepository.findById(userId);
        Optional<Category> category=categoryRepository.findById(categoryId);

        //Vérification si le montant alerte n'est pas supérieur au montant
        double montant = budget.getAmount();
        double montantAlert = budget.getAlertAmount();

        //Vérification si les deux montants sont null
        if(montant == 0.0 || montantAlert == 0.0){
            return ResponseEntity.ok( "Le montant et le montant alerte  doivent être renseignés");
        }
        //Vérification si les deux montants ne sont pas négatifs
        if (montant < 0.0 || montantAlert < 0.0){
            return ResponseEntity.ok("veillez renseignez des montants positifs") ;
        }
        if(montantAlert > montant){
            return ResponseEntity.ok("Le montant alerte ne doit pas être supérieur au montant");
        }


        if (user.isPresent() && category.isPresent()){
            budget.setUser(user.get());
            budget.setRemainder(budget.getAmount());
            budget.setCategory(category.get());
            budgetRepository.save(budget) ;
            return ResponseEntity.ok("budget enregistré avec succès");
        }
        return new ResponseEntity<>("La ressource demandée est introuvable", HttpStatus.NOT_FOUND);
    }

    public List<BudgetProjection> getAllBudgets(Long userId) {
        //return budgetRepository.findAllByUserId(userId);
        return budgetRepository.findAllBudgetsWithUser(userId);
    }
    public List<BudgetProjection> getAllBudgetsTrash(Long userId) {
        //return budgetRepository.findAllByUserId(userId);
        return budgetRepository.findAllBudgetsWithUserTrash(userId);
    }


    public Optional<BudgetProjection> getBudgetByIdAndUserId(Long userId, Long budgetId) {
        //return budgetRepository.findByUserIdAndId(userId, budgetId);
        return budgetRepository.findBudgetWithIdAndUser(budgetId, userId);
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
            return new ResponseEntity<>("La ressource demandée est introuvable", HttpStatus.NOT_FOUND);
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
            return new ResponseEntity<>("La ressource demandée est introuvable", HttpStatus.NOT_FOUND);
        }

        }

    public Object deleteBudget(Long userId, Long budgetId, Budget budget) {
        Optional<Budget> supprimBudget = budgetRepository.findByUserIdAndId(userId,budgetId);
        if (supprimBudget.isPresent()){
            //budgetRepository.deleteByUserIdAndId(userId,budgetId);
            supprimBudget.get().setDelete(true);
            budgetRepository.save(supprimBudget.get());
            return "Budget Supprimé avec succès";
        }else {
            return new ResponseEntity<>("La ressource demandée est introuvable", HttpStatus.NOT_FOUND);
        }
    }

    public List<Budget> getBudgetByAttribut(Long userId, Double montant, Double alertMontant, String titre, Long categorie) {
        return budgetRepository.findByUserIdAndAmountOrAlertAmountOrTitleOrCategoryId
                (userId,montant, alertMontant, titre,categorie);
    }

    public ResponseEntity<Object> transferBudget(Long userId, Long budgetId1, Long budgetId2) {
        Optional<Budget> budgetOptional1 = budgetRepository.findByUserIdAndId(userId, budgetId1);
        Optional<Budget> budgetOptional2 = budgetRepository.findByUserIdAndId(userId, budgetId2);

        if (budgetOptional1.isPresent() && budgetOptional2.isPresent()){
            if(budgetOptional2.get().getCreationDate().getMonth().getValue()>=LocalDate.now().getMonth().getValue()
                    && budgetOptional2.get().getCreationDate().getYear()>=LocalDate.now().getYear()){
                budgetOptional2.get().setRemainder(budgetOptional2.get().getRemainder()+budgetOptional1.get().getRemainder());
                budgetOptional2.get().setAmount(budgetOptional2.get().getAmount()+budgetOptional1.get().getRemainder());
                budgetOptional1.get().setRemainder(0.0);

                Budget budget1 = budgetRepository.save(budgetOptional1.get());
                Budget budget2 = budgetRepository.save(budgetOptional2.get());
                List<Budget> listBudget = new ArrayList<>();
                listBudget.add(budget1);
                listBudget.add(budget2);
                return ResponseEntity.ok(listBudget);
            }
            else {
                return new ResponseEntity<>("Le budget de destination est dépassé.\nChoisissez un budget avec un mois valide", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
    }
}

