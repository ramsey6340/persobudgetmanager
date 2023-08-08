package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.Category;
import com.group3.persobudgetmanager.services.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("users/{userId}/categories")
    @Operation(summary = "Création d'une catégorie pour un utilisateur")
    public ResponseEntity<Object> createCategoryForUser(@PathVariable Long userId, @Valid @RequestBody Category category) {
        return categoryService.createCategoryForUser(userId, category);
    }
    @Operation(summary = "Obtenir les catégories d'un utiisateur")
    @GetMapping("users/{userId}/categories")
    public List<Category> getCategoriesByUser(@PathVariable Long userId) {
        return categoryService.getCategoriesByUser(userId);
    }
    @Operation(summary = "Obtenir une catégorie pour un utilisateur")
    @GetMapping("users/{userId}/categories/{categoryId}")
    public ResponseEntity<Object> getCategory(@PathVariable Long userId, @PathVariable Long categoryId) {
        return categoryService.getCategory(userId, categoryId);
    }

    @Operation(summary = "Obternir les catégories d'un utilisateur par sa description")
    @GetMapping(value = "users/{userId}/categories", params = "description")
    public List<Category> getCategoriesByUserIdAndDescriptionContaining(@PathVariable Long userId, @RequestParam("description") String description) {
        return categoryService.getCategoriesByDescriptionAndUserId(userId, description);
    }

    @Operation(summary = "Obternir les catégories par son titre")
    @GetMapping(value = "users/{userId}/categories", params = "title")
    public List<Category> getCategoriesByUserIdAndTitleContaining(@PathVariable Long userId, @RequestParam("title") String title) {
        return categoryService.getCategoriesByUserIdAndTitleContaining(userId, title);
    }


    @Operation(summary = "Supprimer une catégorie d'un utilisateur")
    @DeleteMapping("users/{userId}/categories/{categoryId}")
    public ResponseEntity<Object> deleteCategoryByUserId(@PathVariable Long userId, @PathVariable Long categoryId) {
        return categoryService.deleteCategoryByUserId(userId, categoryId);
    }

    @Operation(summary = "Mise à jour complet d'une catégorie")
    @PutMapping("users/{userId}/categories/{categoryId}")
    public ResponseEntity<Object> updateCategoryByUserIdWithPut(@PathVariable Long userId, @PathVariable Long categoryId, @Valid @RequestBody Category newCategory) {
        return categoryService.updateCategoryByUserIdWithPut(userId, categoryId, newCategory);
    }

    @Operation(summary = "Mise à jour partielle d'une catégorie")
    @RequestMapping(value = "users/{userId}/categories/{categoryId}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> updateCategoryByUserIdWithPatch(@PathVariable Long userId, @PathVariable Long categoryId, @Valid @RequestBody Map<String, String> partialDate) {
        return categoryService.updateCategoryByUserIdWithPatch(userId, categoryId, partialDate);
    }

}
