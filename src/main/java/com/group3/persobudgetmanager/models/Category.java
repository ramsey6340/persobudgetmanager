package com.group3.persobudgetmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@DynamicUpdate // permet de mettre à jour uniquement la partie modifier
=======

@Entity
@Data
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2
@NoArgsConstructor
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
    @Size(min = 1, max = 30, message = "{Size.category.title}")
    @NotNull(message = "{NotNull.category.title}")
    private String title; // Le titre de la catégorie. Exemple : Loyer

    @Size(min = 1, max = 200, message = "{Size.category.description}")
    private String description;

    @Column(name = "supprimer")
    @JsonIgnore
    private boolean delete=false;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    private User user;
}
