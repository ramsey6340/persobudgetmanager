package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.exceptions.NotFoundException;
import com.group3.persobudgetmanager.models.Category;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.repositories.CategoryRepository;
import com.group3.persobudgetmanager.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<Category> createCategoryForUser(Long userId, Category category) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            category.setUser(user.get());
            categoryRepository.save(category);
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(category.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        }
        return ResponseEntity.notFound().build();
    }
}
