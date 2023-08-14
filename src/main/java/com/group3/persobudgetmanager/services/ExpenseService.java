package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.exceptions.ErrorMessage;
import com.group3.persobudgetmanager.exceptions.NotFoundException;
import com.group3.persobudgetmanager.models.*;
import com.group3.persobudgetmanager.projections.ExpenseProjection;
import com.group3.persobudgetmanager.repositories.*;
import com.group3.persobudgetmanager.tools.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
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
    @Autowired
    EmailRepositoryImpl emailService;

    //la méthode du service pour ajouter une nouvelle dépense
    public ResponseEntity<Object> save(Expense expense, Long userId, Long periodId, Long budgetId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Budget> budget = budgetRepository.findById(budgetId);
        Optional<Period> period = periodRepository.findById(periodId);

        if (user.isPresent() && budget.isPresent() && period.isPresent()) {
            if(expense.getAmount()>0){
                if (budget.get().getRemainder() <= budget.get().getAmount()) {
                    // Définition du format de la date
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate startDateBudget;
                    LocalDate endDateBudget;

                    try {
                        // Conversion de la date chaine en date LocalDate
                        startDateBudget = LocalDate.parse(budget.get().getStartDate(), dateFormatter);
                        endDateBudget = LocalDate.parse(budget.get().getEndDate(), dateFormatter);
                    }catch (DateTimeParseException e){
                        return new ResponseEntity<>("Le format de la date n'est pas correct", HttpStatus.BAD_REQUEST);
                    }

                    // Vérification si le nombre de jours dans l'intervalle de temps du budget est supérieur ou égale
                    // au nombre de jours de la période de la future dépense
                    if (Tools.getNbDay(startDateBudget, endDateBudget) >= period.get().getNbDay()) {
                        if (expense.getAmount() <= budget.get().getRemainder()) {
                            expense.setUser(user.get());
                            expense.setBudget(budget.get());
                            expense.setPeriod(period.get());
                            budget.get().setRemainder(budget.get().getRemainder() - expense.getAmount());

                            // On enregistre la dépense
                            Expense expenseCreated = expenseRepository.save(expense);
                            Budget budgetCreated = budgetRepository.save(budget.get());

                            // On récupère la localisation de la nouvelle dépense
                            URI location = ServletUriComponentsBuilder.
                                    fromCurrentRequest().
                                    path("{id}").
                                    buildAndExpand(expense.getId()).
                                    toUri();

                            // On instancie la notification ici pour pouvoir y acceder en dehors des "if"
                            //List<Notification> notifications = notificationRepository.findAllByBudgetIdAndDeleteFalse(budgetId);

                            // On instancie la notification
                            Notification notification = new Notification();
                            // Vérification si le reliquat a atteint le montant d'alert
                            if (budgetCreated.getRemainder() <= budgetCreated.getAlertAmount()) {

                                notification.setDelete(false);
                                notification.setBudget(budget.get());
                                notification.setUser(user.get());
                                notification.setContent("Alerte: Votre budget est maintenant de "+budget.get().getRemainder());
                                notificationRepository.save(notification);
                                emailService.sendSimpleMail(new EmailDetails(
                                                expenseCreated.getUser().getEmail(),
                                                "Alerte de budget",
                                                "Votre budget est maintenant de " + budget.get().getRemainder()
                                        )
                                );
                            }

                            // Creation d'un body pour la réponse
                            Map<String, Object> responseBody = new HashMap<>();
                            responseBody.put("depense ID", expenseCreated.getId());
                            responseBody.put("depense montant", expenseCreated.getAmount());
                            responseBody.put("categorie", budget.get().getCategory().getTitle());
                            responseBody.put("periode", period.get().getTitle());
                            responseBody.put("reliquat", budget.get().getRemainder());
                            if (!Objects.equals(notification.getContent(), "")){
                                responseBody.put("notification", notification.getContent());
                            }
                            return ResponseEntity.created(location).body(responseBody);
                        }
                        else {
                            return new ResponseEntity<>("Le montant de la dépense est superieur au reliquat", HttpStatus.BAD_REQUEST);
                        }
                    }
                    else {
                        return new ResponseEntity<>("Le nombre de jour de la période choisi dépasse celle du budget", HttpStatus.BAD_REQUEST);
                    }
                }
                else {
                    return new ResponseEntity<>("Le reliquat du budget est superieur au montant du budget", HttpStatus.BAD_REQUEST);
                }
            }
            else{
                return new ResponseEntity<>("Le montant doit être strictement superieur à 0", HttpStatus.NOT_FOUND);
            }
        }
        else{
            //return new ResponseEntity<>("La ressource demander est introuvable", HttpStatus.NOT_FOUND);
            throw new NotFoundException(ErrorMessage.notFound);
        }
    }


    //la méthode du service pour supprimer une dépense
    public ResponseEntity<String> delete(Long expenseId, Long userId){
        Optional<Expense> expenseOptional=expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expenseOptional.isPresent()){

            Budget budget = expenseOptional.get().getBudget();
            budget.setRemainder(budget.getRemainder()+expenseOptional.get().getAmount());
            expenseOptional.get().setDelete(true);

            expenseRepository.save(expenseOptional.get());
            budgetRepository.save(budget);
            return ResponseEntity.ok("Suppression réussi!");
        }else{
            //return new ResponseEntity<>("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
            throw new NotFoundException(ErrorMessage.notFound);
        }
    }

    //la méthode du service pour chercher une dépense
    public ResponseEntity<Object> findById(Long userId, Long id){
        //Optional<Expense> ExpenseOptional = expenseRepository.findByIdAndUserId(id, userId);
        Optional<ExpenseProjection> ExpenseOptional = expenseRepository.findExpenseWithIdAndUserId(userId, id);
        if (ExpenseOptional.isPresent()){
            return ResponseEntity.ok(ExpenseOptional.get());
        }else{
            //return new ResponseEntity<>("La ressource demanade est introuvable!", HttpStatus.NOT_FOUND);
            throw new NotFoundException(ErrorMessage.notFound);
        }
    }

    // la méthode retournant la liste des dépenses
    public List<ExpenseProjection> findAllByUserId(Long id){
        return expenseRepository.findAllExpensesWithUser(id);
    }

    public List<ExpenseProjection> findAllByUserIdTrash(Long id){
        return expenseRepository.findAllExpensesWithUserTrash(id);
    }

    public ResponseEntity<Object> update(Long userId, Long expenseId, Expense expense) {
        Optional<Expense> expenseOptional =expenseRepository.findByIdAndUserId(expenseId, userId);

        if (expenseOptional.isPresent()){
            Budget budget = expenseOptional.get().getBudget();
            if(expense.getAmount() <= budget.getRemainder()){
                Double oldAmount = expenseOptional.get().getAmount();
                Double newAmount = expense.getAmount();

                Double rectification = oldAmount - newAmount;
                Double sum = budget.getRemainder() + rectification;
                budget.setRemainder(sum);
                expenseOptional.get().setAmount(expense.getAmount());
            }
            else{
                return new ResponseEntity<>("Le montant de la dépense est superieur au reliquat", HttpStatus.BAD_REQUEST);
            }

            expenseOptional.get().setAmount(expense.getAmount());
            expenseOptional.get().setPeriod(expense.getPeriod());
            expenseOptional.get().setCreationDate(expense.getCreationDate());
            expenseOptional.get().setDescription(expense.getDescription());

            budgetRepository.save(budget);
            expenseRepository.save(expenseOptional.get());

            return ResponseEntity.ok(expenseRepository.save(expenseOptional.get()));
        } else {
            //return new ResponseEntity("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
            throw new NotFoundException(ErrorMessage.notFound);
        }
    }

    public ResponseEntity<Object> updatePatch (Long userId, Long expenseId, Map<String, Object> expenseMap){
        Optional<Expense> expenseOptional = expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expenseOptional.isPresent()) {
            Budget budget = expenseOptional.get().getBudget();
            if (expenseMap.containsKey("amount")) {
                if((Double) expenseMap.get("amount") <= budget.getRemainder()){
                    Double oldAmount = expenseOptional.get().getAmount();
                    Double newAmount = (Double) expenseMap.get("amount");
                    Double rectification = oldAmount - newAmount;
                    Double sum = budget.getRemainder() + rectification;
                    budget.setRemainder(sum);
                    expenseOptional.get().setAmount((Double) expenseMap.get("amount"));
                }
                else{
                    return new ResponseEntity<>("Le montant de la dépense est superieur au reliquat", HttpStatus.BAD_REQUEST);
                }
            }
            if (expenseMap.containsKey("description")) {
                expenseOptional.get().setDescription((String) expenseMap.get("description"));
            }

            Expense expenseCreated = expenseRepository.save(expenseOptional.get());
            budgetRepository.save(budget);
            return ResponseEntity.ok(expenseCreated);
        } else {
            //return new ResponseEntity<>("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
            throw new NotFoundException(ErrorMessage.notFound);
        }
    }
    public List<Expense> search (Long userId, Double amount, String description, String
    periodTitle, Long budgetId){
        return expenseRepository.findByUserIdAndAmountOrDescriptionContainingOrPeriodTitleContainingOrBudgetId(userId, amount, description, periodTitle, budgetId);
    }

    // Méthode de restauration d'une dépense supprimée
    public ResponseEntity<Object> restoreExpense(Long userId, Long expenseId) {
        Optional<Expense> expenseOptional = expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expenseOptional.isPresent()) {
            expenseOptional.get().setDelete(false);
            return ResponseEntity.ok(expenseRepository.save(expenseOptional.get()));
        } else {
            //return new ResponseEntity<>("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
            throw new NotFoundException(ErrorMessage.notFound);
        }
    }
}