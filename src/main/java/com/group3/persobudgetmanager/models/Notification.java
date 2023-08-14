package com.group3.persobudgetmanager.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

@Entity
@Data
@DynamicUpdate // permet de mettre à jour uniquement la partie modifier
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contenue")
    @NotNull(message = "{NotNull.notification.content}")
    private String content="";

    @NotNull(message = "{NotNull.notification.date}")
    private LocalDate creationDate=LocalDate.now();

    @Column(name = "supprimer")
    @JsonIgnore
    private boolean delete=false;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    //@JsonIgnoreProperties(value = {"fullName", "email", "login", "password"})
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    //@JsonIgnoreProperties(value = {"amount", "alertAmount", "title", "remainder", "creationDate"})
    @JsonIgnore
    private Budget budget;

}
