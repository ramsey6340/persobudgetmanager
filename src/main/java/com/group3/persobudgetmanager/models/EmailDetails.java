package com.group3.persobudgetmanager.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

// Class
public class EmailDetails {

    // Class data members
    private String recipient; // destinateur
    private String msgBody; // corps de l'email
    private String subject; // objet de l'email
    private String attachment; //pièce jointe

    public EmailDetails(String email, String alerteDeBudget, String s) {
        this.recipient=email;
        this.msgBody=s;
        this.subject=alerteDeBudget;
    }
}
