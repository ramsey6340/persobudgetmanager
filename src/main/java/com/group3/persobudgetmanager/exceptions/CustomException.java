package com.group3.persobudgetmanager.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class CustomException{


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object error (MethodArgumentNotValidException exception){

        //Création d'un mappe pour créer les erreurs de validation
        Map<String, Object> responseBody = new HashMap<>();

        //Parcour de toutes les erreurs de validation
        exception.getBindingResult().getAllErrors().forEach(objectError -> {
            responseBody.put(((FieldError)objectError).getField(),objectError.getDefaultMessage());
        });

        //Renvoie de la mappe avec toutes les erreures de validation en tant que réponse json
        return responseBody;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object error(Exception exeption){
        return exeption.getMessage();
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ce login existe déjà");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }




    public static Object notFoundException() {
        // Créer le corps de la réponse avec le format souhaité
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.NOT_FOUND.value());
        responseBody.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        responseBody.put("message", ErrorMessage.notFound);

        return responseBody ;
    }

    public static Object noContentException() {
        // Créer le corps de la réponse avec le format souhaité
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.NO_CONTENT.value());
        responseBody.put("error", HttpStatus.NO_CONTENT.getReasonPhrase());
        responseBody.put("message", ErrorMessage.noContent);

        return responseBody ;
    }
    public static Object resourceAlreadyExist() {
        // Créer le corps de la réponse avec le format souhaité
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.CONFLICT.value());
        responseBody.put("error", HttpStatus.CONFLICT.getReasonPhrase());
        responseBody.put("message", ErrorMessage.alreadyExist);

        return responseBody ;
    }

    public static Object badRequest() {
        // Créer le corps de la réponse avec le format souhaité
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        responseBody.put("message", ErrorMessage.badRequest);

        return responseBody ;
    }
}



