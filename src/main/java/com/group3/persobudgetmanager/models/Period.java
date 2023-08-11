package com.group3.persobudgetmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@DynamicUpdate // permet de mettre à jour uniquement la partie modifier
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
    @NotNull(message = "{NotNull.period.title}")
    @Size(min = 2, max = 30, message = "{Size.period.title}")
    private String title; // Titre du type de period. Exemple : quotidiennes, hebdomadaires ou mensuelles

    @Size(min = 1, max = 200, message = "{Size.period.description}")
    private String description; // une description pour le type de période

    @Column(name = "nb_jour")
    @Min(value = 1, message = "{Min.period.nbDay}")
    @Max(value = 365, message = "{Max.period.nbDay}") // Une année compte au maximum 365 jours
    private int nbDay;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    //@JsonIgnoreProperties(value = {"fullName", "email", "login", "password"})
    @JsonIgnore
    private User user;

    @Column(name = "supprimer")
    @JsonIgnore
    private boolean delete=false;
}
