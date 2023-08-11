package com.group3.persobudgetmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "utilisateur")
public class User {
    /**
     * La classe User est la classe qui répresente l'utilisateur qui utilisera l'API.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Size(min = 2, max = 20, message = "{Size.user.fullName}")
    @NotNull(message = "{NotNull.user.fullName}")
    @Column(name="nom_complet")
    private String fullName;

    @Size(min = 10, max = 30, message = "{Size.user.email}")
    @NotNull(message = "{NotNull.user.email}")
    @Email(message = "{Email.user.email}")
    private String email;

    @NotNull(message = "{NotNull.user.login}")
    @Size(min=2, max = 10, message = "{Size.user.login}")
    private String login;

    @NotNull
    @JsonIgnore
    @Column(name = "supprimer")
    private boolean delete = false;

    @NotNull(message = "{NotNull.user.password}")
    @Size(min = 4, message = "{Size.user.password}")
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Period> periods = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Notification> notifications = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Expense> expenses = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Category> categories = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Budget> budgets = new ArrayList<>();
}
