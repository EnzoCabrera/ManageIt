package com.example.estoque.repositories.CustomerRepositories;

import com.example.estoque.dtos.customerDtos.customerDashboardsDtos.*;
import com.example.estoque.entities.customerEntities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerDashboardRepository extends JpaRepository<Customer, Integer> {

    //Custom query to get active customers
    @Query(value = """
    SELECT COUNT(*) FROM TGVCUS 
    WHERE is_deleted = false
    AND is_active = true;
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

    //Custom query to get top 5 customers with most orders
    @Query(value = """
    SELECT 
        EXTRACT(YEAR FROM o.created_at) AS year, 
        EXTRACT(MONTH FROM o.created_at) AS month, 
        c.cusname AS Cusname,
        c.codcus AS Codcus,
        COUNT(o.codord) AS totalOrders
    FROM 
    TGVCUS c JOIN TGVORD o ON c.codcus = o.codcus
    WHERE 
        c.is_deleted = false
        AND o.is_deleted = false
        AND EXTRACT(YEAR FROM o.created_at) = EXTRACT(YEAR FROM CURRENT_DATE)
        AND EXTRACT(MONTH FROM o.created_at) = EXTRACT(MONTH FROM CURRENT_DATE)
    GROUP BY 
        c.cusname, c.codcus, month, year
    ORDER BY
        totalOrders DESC
    LIMIT 5  
""", nativeQuery = true)
    List<Top5CusMoreOrdDto> findTop5CusMoreOrd();

    //Custom query to get top 5 customers with highest amount spent
    @Query(value = """
    SELECT
        c.cusname AS CusName,
        c.codcus AS CodCus,
        SUM(o.ordcost_in_cents) AS TotalSpent,
        EXTRACT(DAY FROM o.created_at) AS day,
        EXTRACT(YEAR FROM o.created_at) AS year, 
        EXTRACT(MONTH FROM o.created_at) AS month
    FROM 
        TGVCUS c JOIN TGVORD o ON c.codcus = o.codcus
    WHERE 
        c.is_deleted = false
        AND o.is_deleted = false
        AND EXTRACT(DAY FROM o.created_at) = EXTRACT(DAY FROM CURRENT_DATE) 
        AND EXTRACT(YEAR FROM o.created_at) = EXTRACT(YEAR FROM CURRENT_DATE)
        AND EXTRACT(MONTH FROM o.created_at) = EXTRACT(MONTH FROM CURRENT_DATE)
    GROUP BY 
        c.cusname, c.codcus, day, year, month
    ORDER BY
        totalSpent DESC
    LIMIT 5
""", nativeQuery = true)
    List<Top5CusHighestAmountSpent> findTop5CusHighestAmountSpent();

    //Custom query to get all inactive customers
    @Query(value = """
    SELECT 
        c.codcus AS CodCus,
        c.cusname AS CusName,
        c.cusemail AS CusEmail,
        MAX(o.created_at) AS LastOrderDate
    FROM 
        TGVCUS c LEFT JOIN TGVORD o ON c.codcus = o.codcus AND o.is_deleted = false
    WHERE 
        c.is_deleted = false AND c.is_active = true
    GROUP BY 
        c.codcus, c.cusname, c.cusemail
    HAVING 
        MAX(o.created_at) IS NULL 
        OR MAX(o.created_at) < CURRENT_DATE - INTERVAL '2 months'
    ORDER BY
        LastOrderDate NULLS FIRST
""", nativeQuery = true)
    List<InactiveCustomerDto> findInactiveCustomers();
}
