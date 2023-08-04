package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.Category;
import com.group3.persobudgetmanager.repositories.CategoryRepository;
import com.group3.persobudgetmanager.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("users/{userId}/categories")
    public ResponseEntity<Category> createCategoryForUser(@PathVariable Long userId, @RequestBody Category category) {
        return categoryService.createCategoryForUser(userId, category);
    }
}
