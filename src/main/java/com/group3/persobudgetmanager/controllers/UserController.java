package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Creation d'un utilisateur")
    @PostMapping("users")
    public ResponseEntity<User> create(@Valid @RequestBody User user){
        return userService.createUser(user);
    }

    @Operation(summary = "Retourne tous les utilisateurs")
    @GetMapping("users")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @Operation(summary = "Retourne un utilisateur")
    @GetMapping("users/{userId}")
    public Optional<User> getUser(@Valid @PathVariable Long id){
        return userService.getUser(id);
    }

    @Operation(summary = "Modifie un utilisateur")
    @PutMapping("users/{userId}")
    User modifier(@PathVariable Long userId, @RequestBody @Valid User user){
        return userService.modifier(userId, user);
    }

    @Operation(summary = "Supprime un utilisateur")
    @DeleteMapping("users/{userId}")
    public ResponseEntity<Object> delete(@Valid @PathVariable Long id){
        return userService.delete(id);
    }

    @Operation(summary = "Modifie un utilisateur")
    @PatchMapping("users/{userId}")
    public ResponseEntity<User> partialUpdateUser( @Valid @PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return userService.partialUpdateUser(id, updates);
    }

    @Operation(summary = "Se connecter")
    @PostMapping("users/login")
    public ResponseEntity<User> login(@Valid @RequestParam String email, @RequestParam String  password) {
        return userService.login(email, password);
    }
}
