//package com.group3.persobudgetmanager.validators.implementationAnnotations;
/*
import com.group3.persobudgetmanager.validators.annotations.ValidDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.Objects;

// Implémentation de la logique de validation personnalisée pour l'annotation ValidDateLimit
public class ValidDateImplement implements ConstraintValidator<ValidDate, LocalDate> {

    @Override
    public void initialize(ValidDate constraintAnnotation) {
    }

    // Méthode de validation qui vérifie si la date est valide selon les limites spécifiées
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext context) {
        if (Objects.isNull(date)) {
            return false;
        }

        LocalDate currentDate = LocalDate.now();

        // Calcul de la date maximale autorisée : année en cours + 2 ans
        LocalDate maxAllowedDate = currentDate.plusYears(2);

        // Vérification de la validité de la date
        try {
            // Vérification de la validité du mois et du jour dans le mois
            if (date.getMonthValue() <= 12 && date.getYear() <= maxAllowedDate.getYear()) {
                YearMonth yearMonth = YearMonth.of(date.getYear(), date.getMonthValue());
                int maxDaysInMonth = yearMonth.lengthOfMonth();

                return date.getDayOfMonth() <= maxDaysInMonth;
            }
            return false;
        } catch (DateTimeException e) {
            return false; // La date n'est pas valide (mois incorrect)
        }
    }

}*/

