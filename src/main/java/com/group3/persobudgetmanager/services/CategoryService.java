package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.exceptions.CustomException;
import com.group3.persobudgetmanager.exceptions.ErrorMessage;
import com.group3.persobudgetmanager.exceptions.NotFoundException;
import com.group3.persobudgetmanager.exceptions.ResourceAlreadyExist;
import com.group3.persobudgetmanager.models.Category;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.projections.CategoryProjection;
import com.group3.persobudgetmanager.repositories.CategoryRepository;
import com.group3.persobudgetmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.rmi.AlreadyBoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<Object> createCategoryForUser(Long userId, Category category){
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()){
            Optional<Category> categoryExist = categoryRepository.findByUserIdAndTitleAndDeleteFalse(userId, category.getTitle());
            if (categoryExist.isEmpty()){ // Si une catégorie du même nom n'existe pas, on peut créer la catégorie

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
                throw new ResourceAlreadyExist(ErrorMessage.alreadyExist);
            }

        }
        else {
            throw new NotFoundException(ErrorMessage.notFound);
            //return new ResponseEntity<>(CustomException.notFoundException(), HttpStatus.NOT_FOUND);
        }
    }

    public List<CategoryProjection> getCategoriesByUser(Long userId) {
        //return categoryRepository.findAllByUserIdAndDeleteFalse(userId);
        return categoryRepository.findAllCategoriesWithUser(userId);
    }
    public List<CategoryProjection> getCategoriesByUserTrash(Long userId) {
        //return categoryRepository.findAllByUserIdAndDeleteFalse(userId);
        return categoryRepository.findAllCategoriesWithUserTrash(userId);
    }
    public ResponseEntity<Object> getCategory(Long userId, Long categoryId) {
        //Optional<Category> category = categoryRepository.findByIdAndUserIdAndDeleteFalse(categoryId, userId);
        Optional<CategoryProjection> category = categoryRepository.findCategoryWithIdAndUser(categoryId, userId);
        if (category.isPresent()){
            return new ResponseEntity<>(category.get(), HttpStatus.OK);
        }
        else {
            throw new NotFoundException(ErrorMessage.notFound);
        }
    }

    public List<Category> getCategoriesByTitleAndUserId(Long userId, String title) {
        return categoryRepository.findAllByUserIdAndTitleContainingAndDeleteFalse(userId, title);
    }

    public List<Category> getCategoriesByUserIdAndDescriptionContaining(Long userId, String description) {
        return categoryRepository.findAllByUserIdAndDescriptionContainingAndDeleteFalse(userId, description);
    }

    public List<Category> getCategoriesByUserIdAndTitleContaining(Long userId, String title) {
        return categoryRepository.findAllByUserIdAndTitleContainingAndDeleteFalse(userId, title);
    }


    public ResponseEntity<Object> deleteCategoryByUserId(Long userId, Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndUserIdAndDeleteFalse(categoryId, userId);
        if (categoryOptional.isPresent()) {
            categoryOptional.get().setDelete(true);
            return ResponseEntity.ok("Suppression reussi");
        }
        else {
            throw new NotFoundException(ErrorMessage.notFound);
        }
    }

    public List<Category> getCategoriesByDescriptionAndUserId(Long userId, String description) {
        return categoryRepository.findAllByUserIdAndDescriptionContainingAndDeleteFalse(userId, description);
    }


    public ResponseEntity<Object> updateCategoryByUserIdWithPut(Long userId, Long categoryId, Category newCategory) {

        Optional<Category> category = categoryRepository.findByIdAndUserIdAndDeleteFalse(categoryId, userId);
        if (category.isPresent()){
            Category existingCategory = category.get();

            // Debut de la mise à jour
            existingCategory.setTitle(newCategory.getTitle());
            existingCategory.setDescription(newCategory.getDescription());

            Category updateCategory = categoryRepository.save(existingCategory);
            return ResponseEntity.ok(updateCategory);
        }
        throw new NotFoundException(ErrorMessage.notFound);
    }

    public ResponseEntity<Object> updateCategoryByUserIdWithPatch(Long userId, Long categoryId, Map<String, String> partialDate) {

        Optional<Category> category = categoryRepository.findByIdAndUserIdAndDeleteFalse(categoryId, userId);

        if (category.isEmpty()){
            throw new NotFoundException(ErrorMessage.notFound);        }

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

    // Restauration d'une categorie supprimé
    public ResponseEntity<Object> restoreCategory(Long userId, Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findByIdAndUserIdAndDeleteFalse(categoryId, userId);
        if (categoryOptional.isPresent()){
            categoryOptional.get().setDelete(false);
            return ResponseEntity.ok(categoryRepository.save(categoryOptional.get()));
        }
        else {
            throw new NotFoundException(ErrorMessage.notFound);
        }
    }
}
