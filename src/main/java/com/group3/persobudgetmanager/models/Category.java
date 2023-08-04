package com.group3.persobudgetmanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "categorie")
public class Category {
    /**
     * La classe Category répresente la catégorie de dépense.
     * Exemple :alimentation, loyer, transports, divertissement, etc.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre")
    @Size(min = 1, max = 30, message = "")
    @NotNull(message = "")
    private String title; // Le titre de la catégorie. Exemple : Loyer

    @Size(min = 1, max = 200)
    private String description;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User user;
}
