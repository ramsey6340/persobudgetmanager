package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByTitleContaining(String keyword);
    List<Category> findAllByUserIdAndTitleContaining(Long userId, String keyword);
    Optional<Category> findByTitle(String title);
    List<Category> findAllByUserId(Long id);
    List<Category> findAllByDescriptionContaining(String keyword);
    List<Category> findAllByUserIdAndDescriptionContaining(Long userId, String keyword);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
}
