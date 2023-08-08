package com.group3.persobudgetmanager.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


    @Size(min = 2, max = 20, message = "")
    @NotNull(message = "")
    @NotBlank(message = "Le nom ne peut pas etre vide")
    private String fullName;

    @Size
    @NotEmpty(message = "Email cannot be empty")
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "L'email doit être une adresse email valide")
    private String email;

    @NotNull
    @Size(min=2, max = 10)
    private String login;


    @NotNull
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;
}
