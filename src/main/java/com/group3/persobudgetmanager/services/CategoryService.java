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
<<<<<<< HEAD
            Optional<Category> categoryExist = categoryRepository.findByTitleAndDeleteFalse(category.getTitle());
            if (categoryExist.isEmpty()){ // Si une catégorie du même nom n'existe pas, on peut créer la catégorie
=======
            Optional<Category> categoryExist = categoryRepository.findByTitle(category.getTitle());
            if (categoryExist.isEmpty()){ // Si une catégorie du même nom n'existe pas on peut créer la catégorie
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2
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
<<<<<<< HEAD
        return categoryRepository.findAllByUserIdAndDeleteFalse(userId);
    }

    public ResponseEntity<Object> getCategory(Long userId, Long categoryId) {
        Optional<Category> category = categoryRepository.findByIdAndUserIdAndDeleteFalse(categoryId, userId);
=======
        return categoryRepository.findAllByUserId(userId);
    }

    public ResponseEntity<Object> getCategory(Long userId, Long categoryId) {
        Optional<Category> category = categoryRepository.findByIdAndUserId(categoryId, userId);
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2
        if (category.isPresent()){
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(CustomException.notFoundException(), HttpStatus.NOT_FOUND);
        }
    }

    public List<Category> getCategoriesByTitleAndUserId(Long userId, String title) {
<<<<<<< HEAD
        return categoryRepository.findAllByUserIdAndTitleContainingAndDeleteFalse(userId, title);
    }

    public List<Category> getCategoriesByDescriptionAndUserId(Long userId, String description) {
        return categoryRepository.findAllByUserIdAndDescriptionContainingAndDeleteFalse(userId, description);
    }

    public List<Category> getCategoriesByTitle(String title) {
        return categoryRepository.findAllByTitleContainingAndDeleteFalse(title);
    }

    public List<Category> getCategoriesByDescription(String description) {
        return categoryRepository.findAllByDescriptionContainingAndDeleteFalse(description);
    }

    public ResponseEntity<Object> deleteCategoryByUserId(Long userId, Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndUserIdAndDeleteFalse(categoryId, userId);
        if (categoryOptional.isPresent()) {
            categoryOptional.get().setDelete(true);
            categoryRepository.save(categoryOptional.get());
=======
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
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2
            return ResponseEntity.ok("Suppression reussi");
        }
        else {
            return new ResponseEntity<>(CustomException.notFoundException(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<Object> updateCategoryByUserIdWithPut(Long userId, Long categoryId, Category newCategory) {
<<<<<<< HEAD
        Optional<Category> category = categoryRepository.findByIdAndUserIdAndDeleteFalse(categoryId, userId);
=======
        Optional<Category> category = categoryRepository.findByIdAndUserId(categoryId, userId);
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2
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
<<<<<<< HEAD
        Optional<Category> category = categoryRepository.findByIdAndUserIdAndDeleteFalse(categoryId, userId);
=======
        Optional<Category> category = categoryRepository.findByIdAndUserId(categoryId, userId);
>>>>>>> 3539ee3538a406807aca0276a9021da8c17410b2
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
