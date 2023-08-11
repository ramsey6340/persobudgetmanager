package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.models.*;
import com.group3.persobudgetmanager.projections.ExpenseProjection;
import com.group3.persobudgetmanager.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
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

    //la méthode du service pour ajouter une nouvelle dépense
    public ResponseEntity<Object> save(Expense expense, Long userId, Long periodId, Long budgetId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Budget> budget = budgetRepository.findById(budgetId);
        Optional<Period> period = periodRepository.findById(periodId);

        // On instancie la notification ici pour pouvoir y acceder en dehors des "if"
        List<Notification> notifications = notificationRepository.findAllByBudgetIdAndDeleteFalse(budgetId);

        if (user.isPresent() && budget.isPresent() && period.isPresent()) {
            if(expense.getAmount()>0){
                if (budget.get().getRemainder() <= budget.get().getAmount()) {
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate startDate;
                    LocalDate endDate;
                    try {
                        startDate = LocalDate.parse(expense.getStartDate(), dateFormatter);
                        endDate = LocalDate.parse(expense.getEndDate(), dateFormatter);
                    }catch (DateTimeParseException e){
                        return new ResponseEntity<>("Le format de la date n'est pas correct", HttpStatus.BAD_REQUEST);
                    }


                    if (getNbDay(startDate, endDate) == period.get().getNbDay()) {
                        if (expense.getAmount() <= budget.get().getRemainder()) {
                            if (startDate.getMonth() == budget.get().getCreationDate().getMonth()) {
                                if (isValidDate(expense.getStartDate()) && isValidDate(expense.getEndDate())) {
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

                                    if (budgetCreated.getRemainder() <= budgetCreated.getAlertAmount()) {
                                        if (notifications.isEmpty()){
                                            // Enregistrement d'une notification
                                            notifications.add(new Notification());
                                        }
                                        notifications.get(0).setDelete(false);
                                        notifications.get(0).setBudget(budget.get());
                                        notifications.get(0).setUser(user.get());
                                        notifications.get(0).setContent("Alerte: Votre budget est maintenant de "+budget.get().getRemainder());
                                        notifications.set(0, notificationRepository.save(notifications.get(0)));
                                        //sendEmail(expenseCreated.getUser().getEmail(), "Alerte budget", notifications.get(0).getContent());

                                    }

                                    // Creation d'un body pour la réponse
                                    Map<String, Object> responseBody = new HashMap<>();
                                    responseBody.put("depense ID", expenseCreated.getId());
                                    responseBody.put("depense montant", expenseCreated.getAmount());
                                    responseBody.put("categorie", budget.get().getCategory().getTitle());
                                    responseBody.put("periode", period.get().getTitle());
                                    responseBody.put("reliquat", budget.get().getRemainder());
                                    if (!notifications.isEmpty() && !Objects.equals(notifications.get(0).getContent(), "")){
                                        responseBody.put("notification", notifications.get(0).getContent());
                                    }

                                    return ResponseEntity.created(location).body(responseBody);
                                }
                                else {
                                    return new ResponseEntity<>("La date de debut ou de fin est incorrecte", HttpStatus.NOT_FOUND);
                                }
                            }
                            else {
                                return new ResponseEntity<>("Le mois du budget ne correspond pas au mois de la depense", HttpStatus.BAD_REQUEST);
                            }
                        }
                        else {
                            return new ResponseEntity<>("Le montant de la dépense est superieur au reliquat", HttpStatus.BAD_REQUEST);
                        }
                    }
                    else {
                        return new ResponseEntity<>("Le nombre de jour entre la date de debut et de fin est "+getNbDay(startDate, endDate)+" alors que celui de la periode est"+period.get().getNbDay(), HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>("La ressource demander est introuvable", HttpStatus.NOT_FOUND);
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
            return new ResponseEntity<>("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
        }
    }

    //la méthode du service pour chercher une dépense
    public ResponseEntity<Object> findById(Long userId, Long id){
        //Optional<Expense> ExpenseOptional = expenseRepository.findByIdAndUserId(id, userId);
        Optional<ExpenseProjection> ExpenseOptional = expenseRepository.findExpenseWithIdAndUserId(userId, id);
        if (ExpenseOptional.isPresent()){
            return ResponseEntity.ok(ExpenseOptional.get());
        }else{
            return new ResponseEntity<>("La ressource demanade est introuvable!", HttpStatus.NOT_FOUND);
        }
    }
    // la méthode retournant la liste des dépenses
    public List<ExpenseProjection> findAllByUserId(Long id){
        //return expenseRepository.findAllByUserId(id);
        return expenseRepository.findAllExpensesWithUser(id);
    }
    public List<ExpenseProjection> findAllByUserIdTrash(Long id){
        //return expenseRepository.findAllByUserId(id);
        return expenseRepository.findAllExpensesWithUserTrash(id);
    }

    public ResponseEntity<Object> update(Long userId, Long expenseId, Expense expense) {
        Optional<Expense> expense1 =expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expense1.isPresent()){
            Budget budget = expense1.get().getBudget();
            Double oldAmount = expense1.get().getAmount();
            Double newAmount = expense.getAmount();

            Double rectification = oldAmount - newAmount;
            budget.setRemainder(budget.getRemainder()+rectification);


            expense1.get().setAmount(expense.getAmount());
            expense1.get().setPeriod(expense.getPeriod());
            expense1.get().setCreationDate(expense.getCreationDate());
            expense1.get().setDescription(expense.getDescription());

            budgetRepository.save(budget);
            expenseRepository.save(expense1.get());

            return ResponseEntity.ok(expenseRepository.save(expense1.get()));
        } else {
            return new ResponseEntity("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<Object> updatePatch (Long userId, Long expenseId, Map<String, Object> expenseMap){
        Optional<Expense> expenseOptional = expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expenseOptional.isPresent()) {
            Budget budget = expenseOptional.get().getBudget();
            if (expenseMap.containsKey("amount")) {
                Double oldAmount = expenseOptional.get().getAmount();
                Double newAmount = (Double) expenseMap.get("amount");
                Double rectification = oldAmount - newAmount;
                budget.setRemainder(budget.getRemainder() + rectification);
                expenseOptional.get().setAmount((Double) expenseMap.get("amount"));
            }
            if (expenseMap.containsKey("description")) {
                expenseOptional.get().setDescription((String) expenseMap.get("description"));
            }
            if(expenseMap.containsKey("startDate")){
                if (isValidDate(expenseMap.get("startDate").toString())){
                    expenseOptional.get().setStartDate(expenseMap.get("startDate").toString());
                }
                else {
                    return new ResponseEntity<>("La date de debut est incorrecte", HttpStatus.BAD_REQUEST);
                }
            }
            if(expenseMap.containsKey("endDate")){
                if (isValidDate(expenseMap.get("endDate").toString())){
                    expenseOptional.get().setStartDate(expenseMap.get("endDate").toString());
                }
                else {
                    return new ResponseEntity<>("La date de fin est incorrecte", HttpStatus.BAD_REQUEST);
                }
            }
            Expense expenseCreated = expenseRepository.save(expenseOptional.get());
            budgetRepository.save(budget);
            return ResponseEntity.ok(expenseCreated);
        } else {
            return new ResponseEntity<>("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
        }
    }
    public List<Expense> search (Long userId, Double amount, String description, String
    periodTitle, Long budgetId){
        return expenseRepository.findByUserIdAndAmountOrDescriptionContainingOrPeriodTitleContainingOrBudgetId(userId, amount, description, periodTitle, budgetId);
    }

    // Methode pour verifier que le nombre de jours entre startDate et endDate est bien nbDay
    public Long getNbDay (LocalDate startDate, LocalDate endDate){
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }


    // Vérification si la date est valide
    public Boolean isValidDate (String date){
        // Convertir la chaîne en objet LocalDate
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // On récupère les deux derniers caractères de la chaine
        String dayInString = date.substring(date.length() - 2);
        LocalDate localDate;

        // on convertit le jour en int
        int dayInInt = Integer.parseInt(dayInString);
        try {
            // Conversion de la date chaine en date LocalDate
            localDate = LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }

        // Création d'une date constituer de l'année et du mois
        YearMonth yearMonth = YearMonth.of(localDate.getYear(), localDate.getMonthValue());
        if (dayInInt <= yearMonth.lengthOfMonth()) {
            return true;
        } else {
            return false;
        }
    }

    // Méthode de restauration d'une dépense supprimée
    public ResponseEntity<Object> restoreExpense(Long userId, Long expenseId) {
        Optional<Expense> expenseOptional = expenseRepository.findByIdAndUserId(expenseId, userId);
        if (expenseOptional.isPresent()) {
            expenseOptional.get().setDelete(false);
            return ResponseEntity.ok(expenseRepository.save(expenseOptional.get()));
        } else {
            return new ResponseEntity<>("La ressource demandée est introuvable!", HttpStatus.NOT_FOUND);
        }
    }
}