package com.group3.persobudgetmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import java.time.LocalDate;

@Entity
@Data
@DynamicUpdate // permet de mettre à jour uniquement la partie modifier
@NoArgsConstructor
@Table(name = "depense")
public class Expense {
    /**
     * La classe Expense répresente les dépenses de l'utilisateur.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "montant")
    @NotNull(message = "{NotNull.expense.amount}")
    private Double amount; // le montant de la dépense

    @Size(min = 2, max = 200, message = "{Size.expense.note}")
    private String description; // Ajouter une description pour la dépense. Exemple : Cette a été fait pour arranger un ami

    @Column(name = "date_creation")
    private LocalDate creationDate = LocalDate.now(); // La date de création de la dépense. Elle prend toujours la date à laquelle la dépense a été ajouté

    @Column(name = "supprimer")
    @JsonIgnore
    private boolean delete=false;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    //@JsonIgnoreProperties(value = {"fullName", "email", "login", "password"})
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    //@JsonIgnoreProperties(value = {"amount", "alertAmount", "title", "remainder", "creationDate"})
    @JsonIgnore
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "periode_id")
    //@JsonIgnoreProperties(value = {"title", "description", "nbDay"})
    @JsonIgnore
    private Period period;
}
