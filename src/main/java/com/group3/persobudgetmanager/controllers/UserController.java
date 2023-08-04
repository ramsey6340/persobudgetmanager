package com.group3.persobudgetmanager.controllers;


import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.services.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("users")
    public User create(@RequestBody User user){
        return userService.createUser(user);
    }

}
