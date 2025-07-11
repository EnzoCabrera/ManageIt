package com.example.estoque.services;

import com.example.estoque.dtos.expenseDtos.ExpenseRequestDto;
import com.example.estoque.dtos.expenseDtos.ExpenseResponseDto;
import com.example.estoque.entities.expenseEntities.Expense;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.ExpenseMapper;
import com.example.estoque.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseMapper expenseMapper;


    //GET all expenses logic
    public List<ExpenseResponseDto> getAllExpenses(){
        return expenseRepository.findByIsDeletedFalse()
                .stream()
                .map(expenseMapper::toDto)
                .toList();
    }

    //GET expense by date
    public List<ExpenseResponseDto> getExpenseByMonth(int month, int year) {
        List<Expense> expense = expenseRepository.findByMonthAndYear(month, year);
        return expense.stream()
                .map(expenseMapper::toDto)
                .toList();
    }

    //POST expense logic
    public ExpenseResponseDto registerExpense(ExpenseRequestDto dto){
        Expense expense = new Expense();
        expense.setExpdesc(dto.getExpdesc());
        expense.setExpCostInCents(dto.getExpCostInCents());
        expense.setExpdate(dto.getExpdate());
        expense.setExpdatepay(dto.getExpdatepay());
        expense.setExptype(dto.getExpensetype());

        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.toDto(savedExpense);
    }

    //PUT expense logic
    public ExpenseResponseDto updateExpense(Long codexp ,ExpenseRequestDto dto){
        Expense expense = expenseRepository.findById(codexp)
                .orElseThrow(() -> new AppException("Expense not found or deleted.", HttpStatus.NOT_FOUND));

        expense.setExpdesc(dto.getExpdesc());
        expense.setExpCostInCents(dto.getExpCostInCents());
        expense.setExpdate(dto.getExpdate());
        expense.setExpdatepay(dto.getExpdatepay());
        expense.setExptype(dto.getExpensetype());

        Expense updatedExpense = expenseRepository.save(expense);
        return expenseMapper.toDto(updatedExpense);
    }

    //DELETE expense logic
    public ExpenseResponseDto deleteExpense(Long codexp){
        Expense expense = expenseRepository.findBycodexpAndIsDeletedFalse(codexp)
                .orElseThrow(() -> new AppException("Expense not found or already deleted.", HttpStatus.NOT_FOUND));

        expense.setIsDeleted(true);
        Expense deletedExpense = expenseRepository.save(expense);
        return expenseMapper.toDto(deletedExpense);
    }

}
