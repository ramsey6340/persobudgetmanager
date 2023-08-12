package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.models.Budget;
import com.group3.persobudgetmanager.models.Category;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.projections.BudgetProjection;
import com.group3.persobudgetmanager.repositories.BudgetRepository;
import com.group3.persobudgetmanager.repositories.CategoryRepository;
import com.group3.persobudgetmanager.repositories.UserRepository;
import com.group3.persobudgetmanager.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

        if((montant<=0 || montantAlert<=0) || (montantAlert>montant)){
            return new ResponseEntity<>("La valeur du montant ou du montant d'alerte est incorrecte", HttpStatus.BAD_REQUEST);
        }

        if (user.isPresent() && category.isPresent()){

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate;
            LocalDate endDate;
            try {
                startDate = LocalDate.parse(budget.getStartDate(), dateFormatter);
                // Vérification si la date de fin est null. Si oui on calcule automatiquement la date de fin
                if (budget.getEndDate() == null){
                    // Calcule de la date de fin qui est : startDate+30 jours,
                    // le nombre de jours entre startDate et endDate sera donc 31, le premier jour de startDate est compté
                    endDate = startDate.plusDays(30);
                    // Mise à jour de la date de fin
                    budget.setEndDate(endDate.toString());
                }
                else{
                    endDate = LocalDate.parse(budget.getEndDate(), dateFormatter);
                }
            }catch (DateTimeParseException e){
                return new ResponseEntity<>("Le format de la date n'est pas correct", HttpStatus.BAD_REQUEST);
            }

            Optional<Budget> budgetExist = budgetRepository.findAllByUserIdAndCategoryIdAndStartDateAndEndDate(
                    userId, categoryId, budget.getStartDate(), budget.getEndDate());
            if (budgetExist.isPresent()){
                return new ResponseEntity<>("Un budget avec la même date et même catégorie existe déjà", HttpStatus.BAD_REQUEST);
            }

            if (Tools.getNbDay(startDate, endDate) <=31){
                if (Tools.isValidDate(budget.getStartDate()) && Tools.isValidDate(budget.getEndDate())){
                    budget.setUser(user.get());
                    budget.setRemainder(budget.getAmount());
                    budget.setCategory(category.get());
                    Budget budgetCreated = budgetRepository.save(budget) ;

                    URI location = ServletUriComponentsBuilder.
                            fromCurrentRequest().
                            path("{id}").
                            buildAndExpand(budgetCreated.getId()).
                            toUri();
                    return ResponseEntity.created(location).body(budgetCreated);
                }
                else{
                    return new ResponseEntity<>("Le format de la date n'est pas correct", HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return new ResponseEntity<>("Le nombre de jours du budget ne doit pas dépasser 31", HttpStatus.BAD_REQUEST);
            }
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

    public Object updateBudget(Long userId, Long budgetId,  Long categoryId, Budget budget) {
        Optional<Budget> modifBudget = budgetRepository.findByUserIdAndId(userId,budgetId);
        Optional<Category> category=categoryRepository.findById(categoryId);

        if(modifBudget.isPresent() && category.isPresent()){
            modifBudget.get().setTitle(budget.getTitle());
            modifBudget.get().setAmount(budget.getAmount());
            // Ici, j'ai choisi que si on utilise le PUT, alors le reliquat sera réinitialisé à la valeur du montant de la dépense
            modifBudget.get().setRemainder(budget.getAmount());
            if(budget.getAlertAmount() <
                    budget.getAmount()){
                modifBudget.get().setAlertAmount(budget.getAlertAmount());
            }
            else{
                return new ResponseEntity<>("Le montant d'alerte ne doit pas dépasser le montant du budget", HttpStatus.BAD_REQUEST);
            }
            if(category.isPresent()){
                modifBudget.get().setCategory(category.get());
            }
            return budgetRepository.save(modifBudget.get());
        }
        else {
            return new ResponseEntity<>("La ressource demandée est introuvable", HttpStatus.NOT_FOUND);
        }
    }

    public Object patchBudget(Long userId, Long budgetId, Map<String,Object> budgetMap) {
        Optional<Budget> patchingBudget = budgetRepository.findByUserIdAndId(userId, budgetId);

        if (patchingBudget.isPresent()){
            if (budgetMap.containsKey("amount")) {
                Double newAmont = (Double) budgetMap.get("amount");

                // Ici, j'ai choisi que si on utilise le PATCH, alors le reliquat sera mis à jour en fonction de la nouvelle valeur du montant
                Double oldAmount = patchingBudget.get().getAmount();
                Double rectification = newAmont - oldAmount;
                Double remainderAmount = patchingBudget.get().getRemainder()+rectification;
                patchingBudget.get().setRemainder(remainderAmount);

                patchingBudget.get().setAmount((Double) budgetMap.get("amount"));
            }
            if (budgetMap.containsKey("alertAmount")) {
                patchingBudget.get().setAlertAmount((Long) budgetMap.get("alertAmount"));
            }
            if (budgetMap.containsKey("title")) {
                patchingBudget.get().setTitle((String) budgetMap.get("title"));
            }
            if (budgetMap.containsKey("category")) {
                // Avec ça, il faut renseigner l'id de la nouvelle catégorie en utilisant une clé et une valeur, comme pour renseigner les autres attributs
                Optional<Category> categoryOptional = categoryRepository.findById((Long) budgetMap.get("category"));
                categoryOptional.ifPresent(category -> patchingBudget.get().setCategory(category));
            }
            return budgetRepository.save(patchingBudget.get());
        }
        else {
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

