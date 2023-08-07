package com.group3.persobudgetmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
<<<<<<< HEAD
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;
=======
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2

@Entity
@Data
@NoArgsConstructor
<<<<<<< HEAD
@DynamicUpdate
=======
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2
@Table(name = "utilisateur")
public class User {
    /**
     * La classe User est la classe qui répresente l'utilisateur qui utilisera l'API.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_complet")
    @Size(min = 2, max = 20, message = "{Size.user.fullName}")
    @NotNull(message = "{NotNull.user.fullName}")
    private String fullName;

    @Size(min = 10, max = 30, message = "{Size.user.email}")
    @NotNull(message = "{NotNull.user.email}")
    @Email(message = "{Email.user.email}")
    private String email;

    @NotNull(message = "{NotNull.user.fullName}")
    @Size(min=2, max = 10, message = "{Size.user.fullName}")
    private String login;

    @Column(name = "mot_de_passe")
    @NotNull(message = "{NotNull.user.password}")
    @Size(min = 4, message = "{Size.user.fullName}")
    private String password;

    @Column(name = "supprimer")
    @JsonIgnore
    private boolean delete=false;

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Period> periods = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Expense> expenses = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Budget> budgets = new ArrayList<>();

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    List<Notification> notifications = new ArrayList<>();
}
