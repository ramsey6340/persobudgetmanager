package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Category;
import com.group3.persobudgetmanager.projections.CategoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByUserIdAndTitleContainingAndDeleteFalse(Long userId, String keyword);
    List<Category> findAllByUserIdAndDeleteFalse(Long id);
    List<Category> findAllByUserIdAndDescriptionContainingAndDeleteFalse(Long userId, String keyword);
    Optional<Category> findByIdAndUserIdAndDeleteFalse(Long id, Long userId);
    Optional<Category> findByUserIdAndTitleAndDeleteFalse(Long userId, String title);

    @Query("SELECT c.id AS id, c.title AS title, c.description AS description," +
            " u.id AS userId FROM Category c JOIN c.user u WHERE c.user.id=:userId AND c.delete=false")
    List<CategoryProjection> findAllCategoriesWithUser(@Param("userId") Long userId);

    @Query("SELECT c.id AS id, c.title AS title, c.description AS description," +
            " u.id AS userId FROM Category c JOIN c.user u WHERE c.user.id=:userId AND c.id=:categoryId AND c.delete=false")
    Optional<CategoryProjection> findCategoryWithIdAndUser(@Param("categoryId") Long categoryId, @Param("userId") Long userId);
}
