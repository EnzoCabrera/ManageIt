package com.example.estoque.repositories;

import com.example.estoque.entities.expenseEntities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    // Custom query to confirm that the product is not deleted
    Optional<Expense> findBycodexpAndIsDeletedFalse(Long codexp);

}
