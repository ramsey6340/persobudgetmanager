package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.exceptions.ErrorMessage;
import com.group3.persobudgetmanager.models.Period;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.repositories.PeriodRepository;
import com.group3.persobudgetmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PeriodService {

    @Autowired
    private UserRepository userRepository;
    private PeriodRepository periodRepository;

    public ResponseEntity<Object> create(Long userId, Period period) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            period.setUser(userOptional.get());
            Period periodCreated = periodRepository.save(period);
            URI location = ServletUriComponentsBuilder.
                    fromCurrentRequest().
                    path("{id}").
                    buildAndExpand(periodCreated.getId()).
                    toUri();
            return ResponseEntity.created(location).body(periodCreated);
        }
        return new ResponseEntity<>(ErrorMessage.notFound, HttpStatus.NOT_FOUND);
    }

    public List<Period> getPeriodsByUser(Long userId) {
        return periodRepository.findAllByUserIdAndDeleteFalse(userId);
    }

    public ResponseEntity<Object> getPeriodByUser(Long userId, Long periodId) {
        Optional<Period> periodOptional = periodRepository.findByIdAndUserIdAndDeleteFalse(periodId, userId);
        if (periodOptional.isPresent()) {
            return new ResponseEntity<>(periodOptional.get(), HttpStatus.OK);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Object> updatePeriodByUser(Long userId, Long periodId, Period period) {
        Optional<Period> periodOptional = periodRepository.findByIdAndUserIdAndDeleteFalse(userId, periodId);
        if (periodOptional.isPresent()) {
            Period existingPeriod = periodOptional.get();
            existingPeriod.setDescription(period.getDescription());
            existingPeriod.setTitle(period.getTitle());
            existingPeriod.setNbDay(period.getNbDay());
            Period updatePeriod = periodRepository.save(existingPeriod);
            return ResponseEntity.ok(updatePeriod);
        }
        return new ResponseEntity<>(ErrorMessage.notFound, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> deletePeriodByUser(Long userId, Long periodId) {
        Optional<Period> periodOptional = periodRepository.findByIdAndUserIdAndDeleteFalse(periodId, userId);
        if (periodOptional.isPresent()){
            periodRepository.delete(periodOptional.get());
            return new ResponseEntity<>("Suppression reussi", HttpStatus.OK);
        }
        return new ResponseEntity<>(ErrorMessage.notFound, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Object> partialUpdateByUser(Long userId, Long periodId, Map<String, Object> periodMap) {
        Optional<Period> periodOptional = periodRepository.findByIdAndUserIdAndDeleteFalse(periodId, userId);
        if(periodOptional.isPresent()){
            Period existingPeriod = periodOptional.get();

            if(periodMap.containsKey("title")){
                existingPeriod.setTitle((String) periodMap.get("title"));
            }
            if(periodMap.containsKey("description")){
                existingPeriod.setDescription((String) periodMap.get("description"));
            }
            if(periodMap.containsKey("nbDay")){
                existingPeriod.setNbDay((int) periodMap.get("nbDay"));
            }

            Period updatePeriod = periodRepository.save(existingPeriod);
            return new ResponseEntity<>(updatePeriod, HttpStatus.OK);
        }
        return new ResponseEntity<>(ErrorMessage.notFound, HttpStatus.NOT_FOUND);
    }

    public List<Period> getPeriodsByTitle(String tileKeyWord) {
        return periodRepository.findAllByTitleContainingAndDeleteFalse(tileKeyWord);
    }

    public List<Period> getPeriodsByNbDay(int nbDay) {
        return periodRepository.findAllByNbDayAndDeleteFalse(nbDay);
    }
}
