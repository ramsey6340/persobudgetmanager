package com.group3.persobudgetmanager.repositories;


import com.group3.persobudgetmanager.models.User;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String email, String password);

    Optional<User> findByPassword(String password);

    List<User> findAllByDeleteFalse();

    Optional<User> findByIdAndDeleteFalse(Long id);

    Optional<User> findByLogin(String login);
}
