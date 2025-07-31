package com.example.estoque.repositories.ExpenseRepositories;

import com.example.estoque.dtos.expenseDtos.DashboardDtos.*;
import com.example.estoque.entities.expenseEntities.Expense;
import com.example.estoque.entities.expenseEntities.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

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

}
