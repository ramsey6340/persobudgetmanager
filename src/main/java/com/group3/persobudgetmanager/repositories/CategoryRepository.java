package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByTitleContainingAndDeleteFalse(String keyword);
    List<Category> findAllByUserIdAndTitleContainingAndDeleteFalse(Long userId, String keyword);
    Optional<Category> findByTitleAndDeleteFalse(String title);
    List<Category> findAllByUserIdAndDeleteFalse(Long id);
    List<Category> findAllByDescriptionContainingAndDeleteFalse(String keyword);
    List<Category> findAllByUserIdAndDescriptionContainingAndDeleteFalse(Long userId, String keyword);
    Optional<Category> findByIdAndUserIdAndDeleteFalse(Long id, Long userId);
}
