package com.group3.persobudgetmanager.repositories;

import com.group3.persobudgetmanager.models.Period;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeriodRepository extends JpaRepository<Period, Long> {
    List<Period> findAllByUserIdAndDeleteFalse(Long userId);

    Optional<Period> findByIdAndUserIdAndDeleteFalse(Long periodId, Long userId);

    List<Period> findAllByTitleContainingAndDeleteFalse(String tileKeyWord);

    List<Period> findAllByNbDayAndDeleteFalse(int nbDay);
}
