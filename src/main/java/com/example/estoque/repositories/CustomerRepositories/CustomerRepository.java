package com.example.estoque.repositories.CustomerRepositories;

import com.example.estoque.entities.customerEntities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    // Custom query to confirm that the customer is not deleted
    Optional<Customer> findBycodcusAndIsDeletedFalse(Long codcus);

}