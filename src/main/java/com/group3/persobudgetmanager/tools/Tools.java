package com.group3.persobudgetmanager.tools;

import com.group3.persobudgetmanager.models.Budget;
import com.group3.persobudgetmanager.repositories.BudgetRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class Tools {
    @Autowired
    private BudgetRepository budgetRepository;

    // Methode pour verifier que le nombre de jours entre startDate et endDate est bien nbDay
    public static Long getNbDay (LocalDate startDate, LocalDate endDate){
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }


    // Vérification si la date est valide
    public static Boolean isValidDate (String date){
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

    // Methode permettant de verifier si un budget avec une catégorie et un mois spécifique existe déjà dans la base de données
    public boolean isBudgetExist(Long userId, Long categoryId, String startDate, String endDate){
        Optional<Budget> budget = budgetRepository.findAllByUserIdAndCategoryIdAndStartDateAndEndDate(
                userId, categoryId, startDate, endDate
        );
        return budget.isPresent();
    }
}
