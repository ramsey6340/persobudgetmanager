package com.group3.persobudgetmanager.projections;

// Elle est utilisé pour la mise en forme des données du Categorie
public interface CategoryProjection {
    Long getId();
    String getTitle();
    String getDescription();
    Long getUserId();
}
