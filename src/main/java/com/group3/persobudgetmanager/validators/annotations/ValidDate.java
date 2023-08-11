//package com.group3.persobudgetmanager.validators.annotations;
/*
import com.group3.persobudgetmanager.validators.implementationAnnotations.ValidDateImplement;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Cette annotation indique que notre annotation personnalisée peut être utilisée sur les champs, les méthodes et les paramètres
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
// Cette annotation indique que notre annotation personnalisée doit être conservée au moment de l'exécution
@Retention(RetentionPolicy.RUNTIME)
// Cette annotation spécifie que notre annotation personnalisée doit être documentée (utile pour la génération automatique de documentation)
@Documented
// Cette interface déclare les attributs requis pour notre annotation personnalisée
@Constraint(validatedBy = {ValidDateImplement.class})
public @interface ValidDate {

    // Cet attribut permet de fournir un message personnalisé en cas de validation échouée
    String message() default "La date n'est pas valide";

    Class<?>[] groups() default {}; // Spécifiez les groupes appropriés ici

    Class<? extends Payload>[] payload() default {};
}*/