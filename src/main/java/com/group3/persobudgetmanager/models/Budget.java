package com.group3.persobudgetmanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "budget")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    @NotNull(message = "")
    private Long amount; // le montant du budget

    @Column(name = "montant_alert")
    @NotNull(message = "")
    private Long alertAmount; // le montant à partir duquel on doit alerter l'utilisateur si son buget atteint cette somme. Exemple: Si le montant du budget est 100.000 FCFA, il peut dire de lui alerté si cette somme atteint 10.000 FCFA, donc le alertAmount sera 10.000 FCFA et amount sera 100.000 FCFA

    @Column(name = "titre")
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "utilisateur_id")
    private User user;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categorie_id")
    private Category category;
}
