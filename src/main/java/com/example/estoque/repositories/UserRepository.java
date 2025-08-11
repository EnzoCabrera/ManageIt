package com.example.estoque.repositories;

import com.example.estoque.entities.customerEntities.Customer;
import com.example.estoque.entities.userEntities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    UserDetails findByEmail(String email);

    // Custom query to confirm that the user is not deleted
    Optional<User> findByidAndIsDeletedFalse(Long id);
}
