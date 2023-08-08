package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController



@RequestMapping("api/")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "")
    @PostMapping("users")
    public ResponseEntity<User> create(@Valid @RequestBody User user){
        return userService.createUser(user);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }


    @GetMapping("users")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("users/{id}")
    public Optional<User> getUser(@PathVariable Long id){
        return userService.getUser(id);
    }

    @PutMapping("users/{userId}")
    User modifier(@PathVariable Long userId, @RequestBody @Valid User user){
        return userService.modifier(userId, user);
    }

    @DeleteMapping("users/{id}")
    public String delete(@PathVariable Long id){
        return userService.delete(id);
    }

    @PatchMapping("users/{id}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return userService.partialUpdateUser(id, updates);

    }


}
