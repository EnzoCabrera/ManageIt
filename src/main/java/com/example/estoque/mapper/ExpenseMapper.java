package com.example.estoque.mapper;

import com.example.estoque.dtos.expenseDtos.ExpenseResponseDto;
import com.example.estoque.entities.expenseEntities.Expense;
import com.example.estoque.repositories.ExpenseRepository;
import org.springframework.stereotype.Component;

@Component
public class ExpenseMapper {

    private final ExpenseRepository expenseRepository;

    public ExpenseMapper(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public ExpenseResponseDto toDto(Expense expense) {
        ExpenseResponseDto dto = new ExpenseResponseDto();
        dto.setCodexp(expense.getCodexp());
        dto.setExpdesc(expense.getExpdesc());
        dto.setExpCostInCents(expense.getExpCostInCents());
        dto.setExpdate(expense.getExpdate());
        dto.setExpdatepay(expense.getExpdatepay());
        dto.setExptype(expense.getExptype());
        dto.setExpsts(expense.getExpsts());
        dto.setUpdatedBy(expense.getUpdatedBy());
        dto.setUpdatedAt(expense.getUpdatedAt().toString());
        return dto;
    }

}
