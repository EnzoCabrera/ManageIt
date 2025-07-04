package com.example.estoque.repositories;

import com.example.estoque.dtos.expenseDtos.ExpTypeSummaryDto;
import com.example.estoque.dtos.expenseDtos.MonthlyExpSummaryDto;
import com.example.estoque.dtos.expenseDtos.Top5ExpSummaryDto;
import com.example.estoque.entities.expenseEntities.Expense;
import com.example.estoque.entities.expenseEntities.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    // Custom query to confirm that the product is not deleted
    Optional<Expense> findBycodexpAndIsDeletedFalse(Long codexp);

    // Custom query to find expenses that already overdue
    List<Expense> findByExpstsAndExpdatepayBeforeAndIsDeletedFalse(ExpenseStatus expsts, LocalDate date);

    // Custom query to find expenses that overdue tomorrow
    List<Expense> findByExpstsAndExpdatepayAndIsDeletedFalse(ExpenseStatus expsts, LocalDate date);

    // Custom query to find overdue expenses
    List<Expense> findByExpstsAndIsDeletedFalse(ExpenseStatus expsts);

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


}
