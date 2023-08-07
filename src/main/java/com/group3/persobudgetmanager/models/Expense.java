package com.group3.persobudgetmanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "depense")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    /**
     * La classe Expense répresente les dépenses de l'utilisateur.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "montant")
    @NotNull(message = "")
    private double amount; // le montant de la dépense

    @Size(min = 1, max = 200, message = "{Size.expense.note}")
    private String note; // Ajouter une note pour la dépense. Exemple : Cette a été fait pour arranger un ami

    @Column(name = "date_creation")
    private LocalDate creationDate = LocalDate.now(); // La date de création de la dépense. Elle prend toujours la date à laquelle la dépense a été ajouté

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "utilisateur_id")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "periode_id")
    private Period period;
}
