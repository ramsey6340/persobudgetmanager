package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.Period;
import com.group3.persobudgetmanager.projections.PeriodProjection;
import com.group3.persobudgetmanager.services.PeriodService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
public class PeriodController {

    @Autowired
    private PeriodService periodService;

    @Operation(summary = "Créer une nouvelle periode pour un utilisateur")
    @PostMapping("users/{userId}/periods")
    public ResponseEntity<Object> create(@PathVariable Long userId, @Valid @RequestBody Period period){
        return periodService.create(userId, period);
    }

    @Operation(summary = "Récuperer toutes les periodes d'un utilisateur")
    @GetMapping("users/{userId}/periods")
    public List<PeriodProjection> getPeriodsByUser(@PathVariable Long userId) {
        return periodService.getPeriodsByUser(userId);
    }

    @GetMapping("users/{userId}/periods/trash")
    public List<PeriodProjection> getPeriodsByUserTrash(@PathVariable Long userId) {
        return periodService.getPeriodsByUserTrash(userId);
    }

    @Operation(summary = "Récuperer une periode d'un utilisateur")
    @GetMapping("users/{userId}/periods/{periodId}")
    public ResponseEntity<Object> getPeriodByUser(@PathVariable Long userId, @PathVariable Long periodId) {
        return periodService.getPeriodByUser(userId, periodId);
    }

    @Operation(summary = "Mise à jour complete d'une periode")
    @PutMapping("users/{userId}/periods/{periodId}")
    public ResponseEntity<Object> updatePeriodByUser(@PathVariable Long userId, @PathVariable Long periodId, @Valid @RequestBody Period period) {
        return periodService.updatePeriodByUser(userId, periodId, period);
    }

    @Operation(summary = "Suppression d'une periode d'un utilisateur")
    @DeleteMapping("users/{userId}/periods/{periodId}")
    public ResponseEntity<Object> deletePeriodByUser(@PathVariable Long userId, @PathVariable Long periodId) {
        return periodService.deletePeriodByUser(userId, periodId);
    }

    @Operation(summary = "Mise à jour partielle d'une periode d'un utilisateur")
    @RequestMapping(value = "users/{userId}/periods/{periodId}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> partialUpdateByUser(@PathVariable Long userId, @PathVariable Long periodId, @Valid @RequestBody Map<String, Object> periodMap) {
        return periodService.partialUpdateByUser(userId, periodId, periodMap);
    }

    @Operation(summary = "Récuperer les periodes par leur titre")
    @GetMapping(value = "periods/", params = "title")
    public List<Period> getPeriodsByTitle(@RequestParam("title") String tileKeyWord){
        return periodService.getPeriodsByTitle(tileKeyWord);
    }

    @Operation(summary = "Récuperer les periodes par leur titre")
    @GetMapping(value = "periods", params = "nbDay")
    public List<Period> getPeriodsByNbDay(@RequestParam("nbDay") int nbDay){
        return periodService.getPeriodsByNbDay(nbDay);
    }
}
