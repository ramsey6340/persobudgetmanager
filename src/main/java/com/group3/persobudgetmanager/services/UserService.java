package com.group3.persobudgetmanager.services;
import com.group3.persobudgetmanager.Exception.NotFoundException;
import com.group3.persobudgetmanager.models.User;
import com.group3.persobudgetmanager.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


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
        return new ResponseEntity<>(userRepository.save(user), HttpStatus.CREATED);
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
                }).orElseThrow(()->new NotFoundException("Utilisateur non trouvé"));
    }

    public ResponseEntity<Object> delete (Long id){
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
            return ResponseEntity.ok("Suppression reussie");
        }
        throw new NotFoundException("cet utilisateur n'existe pas");
    }


    public ResponseEntity<User> partialUpdateUser(Long id, Map<String, Object> updates) {
        Optional<User> user = userRepository.findByIdAndDeleteFalse(id);
        if (user.isPresent()) {
            // Appliquer les mises à jour fournies dans le Map
            applyPartialUpdates(user.get(), updates);
            User userCreated =userRepository.save(user.get());
            return new ResponseEntity<>(userCreated, HttpStatus.OK);
        }
        throw new NotFoundException("cet utilisateur n'existe pas");
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
        Optional<User> user = userRepository.findByEmailAndPassword(email, password);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
       throw new NotFoundException("cet utilisateur n'existe pas");
    }
}

