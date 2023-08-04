package com.group3.persobudgetmanager.services;

import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }
    List <User> lire(){
        return userRepository.findAll();
    };
}
