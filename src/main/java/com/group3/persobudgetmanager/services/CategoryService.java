package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.exceptions.CustomException;
import com.group3.persobudgetmanager.models.Category;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.repositories.CategoryRepository;
import com.group3.persobudgetmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<Object> createCategoryForUser(Long userId, Category category) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            Optional<Category> categoryExist = categoryRepository.findByTitle(category.getTitle());
            if (categoryExist.isEmpty()){ // Si une catégorie du même nom n'existe pas on peut créer la catégorie
                category.setUser(user.get());
                categoryRepository.save(category);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(category.getId())
                        .toUri();
                return ResponseEntity.created(location).body(category);
            }
            else {
                return new ResponseEntity<>(CustomException.resourceAlreadyExist(), HttpStatus.CONFLICT);
            }

        }
        else {
            return new ResponseEntity<>(CustomException.notFoundException(), HttpStatus.NOT_FOUND);
        }
    }

    public List<Category> getCategoriesByUser(Long userId) {
        return categoryRepository.findAllByUserId(userId);
    }

    public ResponseEntity<Object> getCategory(Long userId, Long categoryId) {
        Optional<Category> category = categoryRepository.findByIdAndUserId(categoryId, userId);
        if (category.isPresent()){
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(CustomException.notFoundException(), HttpStatus.NOT_FOUND);
        }
    }

    public List<Category> getCategoriesByTitleAndUserId(Long userId, String title) {
        return categoryRepository.findAllByUserIdAndTitleContaining(userId, title);
    }

    public List<Category> getCategoriesByDescriptionAndUserId(Long userId, String description) {
        return categoryRepository.findAllByUserIdAndDescriptionContaining(userId, description);
    }

    public List<Category> getCategoriesByTitle(String title) {
        return categoryRepository.findAllByTitleContaining(title);
    }

    public List<Category> getCategoriesByDescription(String description) {
        return categoryRepository.findAllByDescriptionContaining(description);
    }

    public ResponseEntity<Object> deleteCategoryByUserId(Long userId, Long categoryId) {
        Optional<Category> category = categoryRepository.findByIdAndUserId(categoryId, userId);
        if (category.isPresent()) {
            categoryRepository.delete(category.get());
            return ResponseEntity.ok("Suppression reussi");
        }
        else {
            return new ResponseEntity<>(CustomException.notFoundException(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateCategoryByUserIdWithPut(Long userId, Long categoryId, Category newCategory) {
        Optional<Category> category = categoryRepository.findByIdAndUserId(categoryId, userId);
        if (category.isPresent()){
            Category existingCategory = category.get();

            // Debut de la mise à jour
            existingCategory.setTitle(newCategory.getTitle());
            existingCategory.setDescription(newCategory.getDescription());

            Category updateCategory = categoryRepository.save(existingCategory);
            return ResponseEntity.ok(updateCategory);
        }
        return new ResponseEntity<>(CustomException.notFoundException(), HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> updateCategoryByUserIdWithPatch(Long userId, Long categoryId, Map<String, String> partialDate) {
        Optional<Category> category = categoryRepository.findByIdAndUserId(categoryId, userId);
        if (category.isEmpty()){
            return new ResponseEntity<>(CustomException.notFoundException(), HttpStatus.NOT_FOUND);
        }

        Category existingCategory = category.get();

        if(partialDate.containsKey("title")){
            existingCategory.setTitle(partialDate.get("title"));
        }
        if(partialDate.containsKey("description")){
            existingCategory.setDescription(partialDate.get("description"));
        }

        categoryRepository.save(existingCategory);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(existingCategory.getId())
                .toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(location);
        return new ResponseEntity<>(existingCategory, responseHeaders,HttpStatus.OK);
    }
}
