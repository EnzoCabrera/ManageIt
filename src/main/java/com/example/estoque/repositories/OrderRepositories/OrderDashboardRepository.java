package com.example.estoque.repositories.OrderRepositories;

import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyNumberOfOrders;
import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyOrderAvgDto;
import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyOrderSummaryDto;
import com.example.estoque.dtos.orderDtos.DashboardDtos.DailyOrdersDto;
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

    //Custom query to get current day total orders
    @Query(value = """
            SELECT
                COUNT(o.codord) AS totalOrders
            FROM 
                TGVORD o
            WHERE 
                o.is_deleted = false
                AND EXTRACT(DAY FROM o.created_at) = EXTRACT(DAY from CURRENT_DATE)
                AND EXTRACT(MONTH FROM o.created_at) = EXTRACT(MONTH from CURRENT_DATE)
                AND EXTRACT(YEAR FROM o.created_at) = EXTRACT(YEAR from CURRENT_DATE)
""", nativeQuery = true)
    List<DailyNumberOfOrders> getDailyNumberOfOrders();

    //Custom query to get current day orders average cost
    @Query(value = """
            SELECT 
                 EXTRACT(DAY FROM o.created_at) AS day,
                 EXTRACT(MONTH FROM o.created_at) AS month,
                 EXTRACT(YEAR FROM o.created_at) AS year,
                 AVG(o.ordcost_in_cents) AS avgCostInCents
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
    List<DailyOrderAvgDto> getDailyOrderAverage();

    //Custom query to get current day orders
    @Query(value = """
            SELECT
                 EXTRACT(DAY FROM o.created_at) AS day,
                 EXTRACT(MONTH FROM o.created_at) AS month,
                 EXTRACT(YEAR FROM o.created_at) AS year,
                 o.codord AS codord,
                 o.created_by AS createdBy,
                 o.ordcost_in_cents AS TotalInCents,
                 TO_CHAR(o.created_at, 'HH24:MI:SS') AS createdAt,
                 o.ordsts AS ordsts,
                 c.cusname AS cusname
            FROM
                 TGVORD o 
            JOIN 
                 TGVCUS c ON o.codcus = c.codcus
            WHERE
                 o.is_deleted = false
                 AND EXTRACT(DAY FROM o.created_at) = EXTRACT(DAY from CURRENT_DATE)
                 AND EXTRACT(MONTH FROM o.created_at) = EXTRACT(MONTH from CURRENT_DATE)
                 AND EXTRACT(YEAR FROM o.created_at) = EXTRACT(YEAR from CURRENT_DATE)
            ORDER BY
                 day, month, year
""", nativeQuery = true)
    List<DailyOrdersDto> getDailyOrders();
}
