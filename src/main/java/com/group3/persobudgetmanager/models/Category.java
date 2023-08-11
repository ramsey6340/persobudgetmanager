package com.group3.persobudgetmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@DynamicUpdate // permet de mettre à jour uniquement la partie modifier
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

    @NotNull(message = "Pas null") //{NotNull.category.title}
    @Size(min = 1, max = 30, message = "{Size.category.title}")
    @Column(name = "titre")
    private String title; // Le titre de la catégorie. Exemple : Loyer

    @Size(min = 1, max = 200, message = "{Size.category.description}")
    private String description;

    @Column(name = "supprimer")
    @JsonIgnore
    private boolean delete=false;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    //@JsonIgnoreProperties(value = {"fullName", "email", "login", "password"})
    @JsonIgnore
    private User user;
}
