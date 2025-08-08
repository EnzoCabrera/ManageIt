package com.example.estoque.repositories;

import com.example.estoque.entities.userEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    UserDetails findByEmail(String email);
}
