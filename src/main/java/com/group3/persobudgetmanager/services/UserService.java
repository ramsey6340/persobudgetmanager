package com.group3.persobudgetmanager.services;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.AbstractPersistable_;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<User> createUser(User user) {
        User user1 =userRepository.save(user);
        return new ResponseEntity<>(user1, HttpStatus.CREATED);


    }
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id){
        return userRepository.findById(id);
    }


    public User modifier(Long userId, User user) {
        return userRepository.findById(userId)
                .map(u->{
                    u.setFullName(user.getFullName());
                    u.setEmail(user.getEmail());
                    u.setLogin(user.getLogin());
                    u.setPassword(user.getPassword());
                    return userRepository.save(u);
                }).orElseThrow(()->new RuntimeException("User not found with ID:" +id));
    }

    public String delete (Long id){
       userRepository.deleteById(id);
           return "User was deleted successfuly";
        }


    public ResponseEntity<User> partialUpdateUser(Long id, Map<String, Object> updates) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null) {
            // Appliquer les mises à jour fournies dans le Map
            applyPartialUpdates(user, updates);
              User user1 =userRepository.save(user);
            return new ResponseEntity<>(user1, HttpStatus.OK);

        }

        return null;
    }

    private void applyPartialUpdates(User user, Map<String, Object> updates) {
        if (updates.containsKey("fullName")) {
            user.setFullName((String) updates.get("fullName"));
        }

        if (updates.containsKey("email")) {
            user.setEmail((String) updates.get("email"));
        }

        // Appliquer d'autres mises à jour selon les attributs que vous souhaitez autoriser à mettre à jour partiellement
    }
}

