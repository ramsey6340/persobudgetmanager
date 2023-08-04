package com.group3.persobudgetmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistException extends RuntimeException{
    ResourceAlreadyExistException(String message) {
        super(message);
    }
}
