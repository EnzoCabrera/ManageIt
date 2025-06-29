package com.example.estoque.repositories;

import com.example.estoque.entities.expenseEntities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Custom query to confirm that the product is not deleted
    Optional<Expense> findBycodexpAndIsDeletedFalse(Long codexp);

    // Custom query to find all non-deleted stocks
    List<Expense> findByIsDeletedFalse();

    // Custom query find the expense by month and to confirm that the product is not deleted by
    @Query("""
    SELECT e FROM Expense e
    WHERE EXTRACT(MONTH FROM e.expdate) = :month
      AND EXTRACT(YEAR FROM e.expdate) = :year
      AND e.isDeleted = false
""")
    List<Expense> findByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
