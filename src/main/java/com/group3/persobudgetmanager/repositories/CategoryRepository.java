package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByTitleContaining(String keyword);
    List<Category> findAllByUserId(Long id);
    List<Category> findAllByDescriptionContaining(String keyword);
}
