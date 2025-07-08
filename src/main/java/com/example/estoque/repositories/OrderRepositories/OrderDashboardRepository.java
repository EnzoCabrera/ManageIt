package com.example.estoque.repositories.OrderRepositories;

import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyOrderSummaryDto;
import com.example.estoque.entities.OrderEntities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDashboardRepository extends JpaRepository<Order, Long> {

    //Custom query to get current day orders total cost
    @Query(value = """
            SELECT 
                 EXTRACT(DAY FROM o.created_at) AS day,
                 EXTRACT(MONTH FROM o.created_at) AS month,
                 EXTRACT(YEAR FROM o.created_at) AS year,
                 SUM(o.ordcost_in_cents) AS totalInCents
            FROM 
                 TGVORD o
            WHERE
                 o.is_deleted = false
                 AND EXTRACT(DAY FROM o.created_at) = EXTRACT(DAY from CURRENT_DATE)
                 AND EXTRACT(MONTH FROM o.created_at) = EXTRACT(MONTH from CURRENT_DATE)
                 AND EXTRACT(YEAR FROM o.created_at) = EXTRACT(YEAR from CURRENT_DATE)
            GROUP BY
                    day, month, year
            ORDER BY
                    day, month, year
            """, nativeQuery = true)
    List<DailyOrderSummaryDto> getDailyOrderSummary();
}
