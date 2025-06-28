package com.example.estoque.repositories;

import com.example.estoque.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Custom query to confirm that the customer is not deleted
    Optional<Customer> findBycodcusAndIsDeletedFalse(Long codcus);

    // Custom query to find all non-deleted customers
    List<Customer> findByIsDeletedFalse();
}
