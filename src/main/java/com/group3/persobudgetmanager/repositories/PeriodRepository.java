package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Period;
import com.group3.persobudgetmanager.projections.ExpenseProjection;
import com.group3.persobudgetmanager.projections.PeriodProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {
    List<Period> findAllByUserIdAndDeleteFalse(Long userId);
    Optional<Period> findByIdAndUserIdAndDeleteFalse(Long periodId, Long userId);
    List<Period> findAllByTitleContainingAndDeleteFalse(String tileKeyWord);
    List<Period> findAllByNbDayAndDeleteFalse(int nbDay);

    @Query("SELECT p.id, p.title, p.description, p.nbDay, u.fullName FROM Period p JOIN p.user u WHERE p.user.id=:userId AND p.delete = false")
    List<PeriodProjection> findAllPeriodsWithUser(@Param("userId") Long userId);

    @Query("SELECT p.id, p.title, p.description, p.nbDay, u.fullName FROM Period p JOIN p.user u WHERE p.user.id=:userId AND p.id=:periodId AND p.delete = false")
    Optional<PeriodProjection> findPeriodWithUser(@Param("userId") Long userId, @Param("periodId") Long periodId);
}
