package com.example.estoque.repositories.ExpenseRepositories;

import com.example.estoque.dtos.expenseDtos.DashboardDtos.*;
import com.example.estoque.entities.expenseEntities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseDashboardRepository extends JpaRepository<Expense, Long> {

    //Custom query to get current month expenses
    @Query(value = """
            SELECT
                EXTRACT(YEAR FROM e.expdate) AS year,
                EXTRACT(MONTH FROM e.expdate) AS month,
                SUM(e.expcost_in_cents) AS totalInCents
            FROM
                TGVEXP e
            WHERE
                e.is_deleted = false
                AND EXTRACT(YEAR FROM e.expdate) = EXTRACT(YEAR FROM CURRENT_DATE)
                AND EXTRACT(MONTH FROM e.expdate) = EXTRACT(MONTH FROM CURRENT_DATE)  
            GROUP BY
                year, month
            ORDER BY
                year, month 
       """, nativeQuery = true)
    List<MonthlyExpSummaryDto> findMonthlyExpenseSummary();


    //Custom query to get current month expenses by type
    @Query(value = """
        SELECT e.exptype AS exptype, SUM(e.expcost_in_cents) AS totalInCents
        FROM TGVEXP e
        WHERE e.is_deleted = false
          AND EXTRACT(MONTH FROM e.expdate) = EXTRACT(MONTH FROM CURRENT_DATE)
          AND EXTRACT(YEAR FROM e.expdate) = EXTRACT(YEAR FROM CURRENT_DATE)
        GROUP BY e.exptype
    """, nativeQuery = true)
    List<ExpTypeSummaryDto> getExpTypeSummary();

    //Custom query to get current month top 5 expenses
    @Query(value = """
        SELECT e.codexp AS codexp,
               e.expdesc AS expdesc,
               e.expcost_in_cents AS totalInCents,
               e.exptype AS exptype,
               e.expdate AS expdate,
               e.expdatepay AS expdatepay
        FROM TGVEXP e
        WHERE e.is_deleted = false
            AND EXTRACT(MONTH FROM e.expdate) = EXTRACT(MONTH FROM CURRENT_DATE)
            AND EXTRACT(YEAR FROM e.expdate) = EXTRACT(YEAR FROM CURRENT_DATE)
         ORDER BY e.expcost_in_cents DESC
            LIMIT 5
""", nativeQuery = true)
    List<Top5ExpSummaryDto> getTop5ExpenseSummary();

    //Custom query to get current month expenses by status
    @Query(value = """
    SELECT e.expsts AS expsts,
           COUNT(*) AS totalCount,
           SUM(expcost_in_cents) AS totalInCents
    FROM TGVEXP e 
    WHERE e.is_deleted = false
        AND EXTRACT(MONTH FROM e.expdate) = EXTRACT(MONTH FROM CURRENT_DATE)
        AND EXTRACT(YEAR FROM e.expdate) = EXTRACT(YEAR FROM CURRENT_DATE)
    GROUP BY expsts
""", nativeQuery = true)
    List<ExpSummaryByStsDto> getExpSummaryBySts();

    //Custom query to get all expenses unpaid
    @Query(value = """
        SELECT exptype AS exptype,
               COUNT(*) AS Count,
               SUM(expcost_in_cents) AS totalInCents
        FROM TGVEXP e
        WHERE 
            expdatepay > CURRENT_DATE 
            AND e.expsts IN ('PENDING', 'OVERDUE')
            AND is_deleted = false
        GROUP BY exptype
        ORDER BY exptype
    """, nativeQuery = true)
    List<FutureExpenseSummaryDto> getFutureExpensesGrouped();
}
