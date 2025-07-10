package com.example.estoque.repositories.CustomerRepositories;

import com.example.estoque.dtos.customerDtos.customerDashboardsDtos.ActiveCustomersDto;
import com.example.estoque.entities.customerEntities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerDashboardRepository extends JpaRepository<Customer, Integer> {

    //Custom query to get active customers
    @Query(value = """
    SELECT COUNT(*) FROM TGVCUS 
    WHERE is_deleted  = false;
""", nativeQuery = true)
     Integer findAllActiveCus();
}
