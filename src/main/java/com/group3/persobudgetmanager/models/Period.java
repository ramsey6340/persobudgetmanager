package com.group3.persobudgetmanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "periode")
public class Period {
    /**
     * La classe PeriodType répresente la période de temps. Exemple : Quotidienne, Hebdommandaire, Mensuelle
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre")
    @NotNull(message = "")
    @Size(min = 2, max = 30)
    private String title; // Titre du type de period. Exemple : quotidiennes, hebdomadaires ou mensuelles

    private String description; // une description pour le type de période

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User user;
}
