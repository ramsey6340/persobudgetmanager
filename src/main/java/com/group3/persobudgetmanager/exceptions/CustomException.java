package com.group3.persobudgetmanager.exceptions;

import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class CustomException extends RuntimeException{

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
}
