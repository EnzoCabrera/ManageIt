package com.example.estoque.repositories.CustomerRepositories;

import com.example.estoque.dtos.customerDtos.customerDashboardsDtos.ActiveCustomersDto;
import com.example.estoque.dtos.customerDtos.customerDashboardsDtos.NewCusPerMonthDto;
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

    //Custom query to get new customers per month
    @Query(value = """
    SELECT 
        EXTRACT(YEAR FROM c.created_at) AS year, 
        EXTRACT(MONTH FROM c.created_at) AS month, 
        COUNT(*) AS totalNewCus
    FROM TGVCUS c
    WHERE 
        c.is_deleted = false
        AND EXTRACT(YEAR FROM c.created_at) = EXTRACT(YEAR FROM CURRENT_DATE)
        AND EXTRACT(MONTH FROM c.created_at) = EXTRACT(MONTH FROM CURRENT_DATE)
    GROUP BY
        year, month
    ORDER BY
        year, month
""", nativeQuery = true)
    List<NewCusPerMonthDto> findNewCusPerMonth();
}
