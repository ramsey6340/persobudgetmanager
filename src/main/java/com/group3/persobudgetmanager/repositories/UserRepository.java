package com.group3.persobudgetmanager.repositories;


import com.group3.persobudgetmanager.models.User;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
