package com.group3.persobudgetmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import org.hibernate.annotations.DynamicUpdate;
=======
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2

import java.time.LocalDate;

@Entity
@Data
<<<<<<< HEAD
@DynamicUpdate // permet de mettre à jour uniquement la partie modifier
=======
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2
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
<<<<<<< HEAD
    @NotNull(message = "{NotNull.expense.amount}")
=======
    @NotNull(message = "")
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2
    private Double amount; // le montant de la dépense

    @Size(min = 2, max = 200, message = "{Size.expense.note}")
    private String description; // Ajouter une description pour la dépense. Exemple : Cette a été fait pour arranger un ami

    @Column(name = "date_creation")
    private LocalDate creationDate = LocalDate.now(); // La date de création de la dépense. Elle prend toujours la date à laquelle la dépense a été ajouté

    @Column(name = "date_debut")
    @NotNull(message = "{NotNull.expense.startDate}")
    @Past(message = "")
    private LocalDate startDate;

    @Column(name = "date_fin")
    @NotNull(message = "{NotNull.expense.endDate}")
    @Past(message = "")
    private LocalDate endDate;

    @Column(name = "supprimer")
    @JsonIgnore
    private boolean delete=false;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "periode_id")
    private Period period;
}
