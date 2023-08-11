package com.group3.persobudgetmanager.services;
import com.group3.persobudgetmanager.Exception.NotFoundException;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.repositories.*;
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
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    private PeriodRepository periodRepository;

    public ResponseEntity<User> createUser(User user) {
        User user1 =userRepository.save(user);
        return new ResponseEntity<>(user1, HttpStatus.CREATED);


    }
    public List<User> getUsers(){
        return userRepository.findAllByDeleteFalse();
    }

    public Optional<User> getUser(Long id){
        return userRepository.findByIdAndDeleteFalse(id);
    }


    public User modifier(Long userId, User user) {
        return userRepository.findByIdAndDeleteFalse(userId)
                .map(u->{
                    u.setFullName(user.getFullName());
                    u.setEmail(user.getEmail());
                    u.setLogin(user.getLogin());
                    u.setPassword(user.getPassword());
                    return userRepository.save(u);
                }).orElseThrow(()->new RuntimeException("User not found with ID:" +id));
    }

    public String delete (Long id){
        Optional<User> userOptional=userRepository.findByIdAndDeleteFalse(id);
        if (userOptional.isPresent()) {
            userOptional.get().setDelete(true);
            userOptional.get().getBudgets().forEach(b-> {
                b.setDelete(true);
                budgetRepository.save(b);
            });

            userOptional.get().getCategories().forEach(c->{
                c.setDelete(true);
                categoryRepository.save(c);
            });

            userOptional.get().getExpenses().forEach(e->{
                e.setDelete(true);
                expenseRepository.save(e);
            });

            userOptional.get().getPeriods().forEach(p->{
                p.setDelete(true);
                periodRepository.save(p);
            });
            userRepository.save(userOptional.get());
            return "Suppression reussie";
        }
        return "Utilisateur non trouvé";
    }


    public ResponseEntity<User> partialUpdateUser(Long id, Map<String, Object> updates) {
        User user = userRepository.findByIdAndDeleteFalse(id).orElse(null);

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

        if (updates.containsKey("login")){
            user.setLogin((String) updates.get("login"));
        }

        if (updates.containsKey("password")){
            user.setPassword((String) updates.get("password"));
        }

    }

    public ResponseEntity<User> login(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
       throw new NotFoundException("cet utilisateur n'existe pas");
    }
}

