package com.group3.persobudgetmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@DynamicUpdate // permet de mettre à jour uniquement la partie modifier
@NoArgsConstructor
@Table(name = "budget")
public class Budget {
    /**
     * La classe Budget répresente le budget de l'utilisateur pour différentes catégories de dépenses.
     * Exemple : L'utilisateur pourra définir un budget mensuel pour les courses alimentaires,
     * un autre pour les divertissements,loyer,transports etc.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "montant")
    @NotNull(message = "{NotNull.budget.amount}")
    private Double amount; // le montant du budget

    @Column(name = "montant_alert")
    @NotNull(message = "{NotNull.budget.alertAmount}")
    private Long alertAmount; // Le montant à partir duquel on doit alerter l'utilisateur si son buget atteint cette somme. Exemple: Si le montant du budget est 100.000 FCFA, il peut dire de lui alerté si cette somme atteint 10.000 FCFA, donc le alertAmount sera 10.000 FCFA et amount sera 100.000 FCFA

    @Column(name = "titre")
    @Size(max = 50, message = "{Size.budget.title}")
    private String title;

    @Column(name = "supprimer")
    @JsonIgnore
    private boolean delete=false;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Category category;
}
