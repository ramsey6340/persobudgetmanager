package com.group3.persobudgetmanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "utilisateur")
public class User {
    /**
     * La classe User est la classe qui répresente l'utilisateur qui utilisera l'API.
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_complet")
    @Size(min = 2, max = 20, message = "")
    @NotNull(message = "")
    private String fullName;

    @Size(min = 10, max = 30, message = "")
    @NotNull(message = "")
    private String email;

    @NotNull
    @Size(min=2, max = 10)
    private String login;

    @Column(name = "mot_de_passe")
    @NotNull
    @Size(min = 4)
    private String password;
}
