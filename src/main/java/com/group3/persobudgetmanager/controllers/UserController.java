package com.group3.persobudgetmanager.controllers;

import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Valid

@RestController



@RequestMapping("api/")
public class UserController {

    @Autowired
    private UserService userService;




    @PostMapping("users")
    public ResponseEntity<User> create(@Valid @RequestBody User user){
        if (user.getEmail().isEmpty()){

            return null;
        }
        return userService.createUser(user);
    }



    @GetMapping("users")
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("users/{id}")
    public Optional<User> getUser(@Valid @PathVariable Long id){
        return userService.getUser(id);
    }

    @PutMapping("users/{userId}")
    User modifier(@PathVariable Long userId, @RequestBody @Valid User user){
        return userService.modifier(userId, user);
    }

    @DeleteMapping("users/{id}")
    public String delete(@Valid @PathVariable Long id){
        return userService.delete(id);
    }

    @PatchMapping("users/{id}")
    public ResponseEntity<User> partialUpdateUser( @Valid @PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return userService.partialUpdateUser(id, updates);

    }

    @PostMapping("users/login")
    public ResponseEntity <User> login(@Valid @RequestParam String email, @RequestParam String  password) {
        return userService.login(email, password);
    }


}
