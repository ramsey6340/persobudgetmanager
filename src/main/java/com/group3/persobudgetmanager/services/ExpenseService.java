package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.models.*;
import com.group3.persobudgetmanager.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
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
    @Autowired
    private NotificationRepository notificationRepository;

    //la méthode du service pour ajouter une nouvelle dépense
    public ResponseEntity<Object> save(Expense expense, Long userId, Long periodId, Long budgetId){
        Optional<User> user = userRepository.findById(userId);
        Optional<Budget> budget = budgetRepository.findById(budgetId);
        Optional<Period> period = periodRepository.findById(periodId);
        if(user.isPresent() && budget.isPresent() && period.isPresent()){
           if (budget.get().getRemainder()!=0){
               if (getNbDay(expense.getStartDate(), expense.getEndDate()) == period.get().getNbDay()){
                   if(expense.getAmount() <= budget.get().getRemainder()) {
                       if(expense.getStartDate().getMonth() == budget.get().getCreationDate().getMonth()) {
                           expense.setUser(user.get());
                           expense.setUserFullName(user.get().getFullName());
                           expense.setBudget(budget.get());
                           expense.setPeriod(period.get());
                           budget.get().setRemainder(budget.get().getRemainder()-expense.getAmount());

                           if(budget.get().getRemainder()<=budget.get().getAlertAmount()) {
                               Notification notification = new Notification();
                               notification.setDelete(false);
                               notification.setBudget(budget.get());
                               notification.setUser(user.get());
                               notification.setContent("Alerte:Vous avez atteint le reliquat de votre budget");
                               notificationRepository.save(notification);
                           }
                           budgetRepository.save(budget.get());
                           expenseRepository.save(expense);

                           // On récupère la localisation de la nouvelle depense
                           URI location = ServletUriComponentsBuilder.
                                   fromCurrentRequest().
                                   path("{id}").
                                   buildAndExpand(expense.getId()).
                                   toUri();

                           // Creation d'un body pour la réponse
                           Map<String, Object> responseBody = new HashMap<>();
                           responseBody.put("depense montant", expense.getAmount());
                           responseBody.put("categorie", budget.get().getCategory().getTitle());
                           responseBody.put("periode", period.get().getTitle());
                           responseBody.put("reliquat", budget.get().getRemainder());

                           return ResponseEntity.created(location).body(responseBody);
                       }
                       else {
                           return new ResponseEntity<>("Le mois du budget ne correspond pas au mois de la depense", HttpStatus.BAD_REQUEST);
                       }
                   }
                   else{
                       return new ResponseEntity<>("Le montant du budget est supperieur au reliquat", HttpStatus.BAD_REQUEST);
                   }
               }
               else {
                   return new ResponseEntity<>("Le nombre de jour dans l'interval de temps est "+getNbDay(expense.getStartDate(), expense.getEndDate())+" alors que celui de la periode de la depense est "+period.get().getNbDay()+"", HttpStatus.BAD_REQUEST);
               }
           }
           else {
               return new ResponseEntity<>("Le reliquat du budget est 0", HttpStatus.BAD_REQUEST);
           }
        }
        else{
            return new  ResponseEntity<> ("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
        }
    }


    //la méthode du service pour supprimer une dépense
    public ResponseEntity<String> delete(Long expenseId, Long userId){
        Optional<Expense> expenseOptional=expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expenseOptional.isPresent()){
            expenseRepository.delete(expenseOptional.get());
            return ResponseEntity.ok("Supprission réussi!");
        }else{
            return new ResponseEntity<>("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
        }
    }

    //la méthode du service pour chercher une dépense
    public ResponseEntity<Object> findById(Long userId, Long id){
        Optional<Expense> ExpenseOptional = expenseRepository.findByIdAndUserId(id, userId);
        if (ExpenseOptional.isPresent()){
            return ResponseEntity.ok(ExpenseOptional.get());
        }else{
            return new ResponseEntity<>("La ressource demanade est introuvable!", HttpStatus.NOT_FOUND);
        }
    }
    // la méthode retournant la liste des dépenses
    public List<Expense> findAllByUserId(Long id){
        return expenseRepository.findAllByUserId(id);
    }

    public ResponseEntity<Object> update(Long userId, Long expenseId, Expense expense) {
        Optional<Expense> expense1 =expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expense1.isPresent()){
            expense1.get().setAmount(expense.getAmount());
            expense1.get().setPeriod(expense.getPeriod());
            expense1.get().setCreationDate(expense.getCreationDate());
            expense1.get().setDescription(expense.getDescription());
            return ResponseEntity.ok(expenseRepository.save(expense1.get()));
        }else{
            return new ResponseEntity("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
        }

        }

    public ResponseEntity<Object> updatePatch(Long userId, Long expenseId, Map<String, Object> expenseMap) {
        Optional<Expense> expenseOptional = expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expenseOptional.isPresent()){
            if (expenseMap.containsKey("amount")){
                expenseOptional.get().setAmount((Double) expenseMap.get("amount"));
            }
            if (expenseMap.containsKey("description")){
                expenseOptional.get().setDescription((String) expenseMap.get("description"));
            }
            return ResponseEntity.ok(expenseRepository.save(expenseOptional.get()));
        }else{
            return new ResponseEntity<>("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
        }
        }
        public List<Expense> search(Long userId, Double amount, String description, String periodTitle, Long budgetId){
         return expenseRepository.findByUserIdAndAmountOrDescriptionContainingOrPeriodTitleContainingOrBudgetId(userId,amount,description, periodTitle, budgetId);
        }

        // Methode pour verifier que le nombre de jours entre startDate et endDate est bien nbDay
        Long getNbDay(LocalDate startDate, LocalDate endDate) {
            return ChronoUnit.DAYS.between(startDate, endDate)+1;
        }
    }

