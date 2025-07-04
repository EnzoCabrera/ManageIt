package com.example.estoque.controllers;

import com.example.estoque.dtos.expenseDtos.ExpenseRequestDto;
import com.example.estoque.dtos.expenseDtos.ExpenseResponseDto;
import com.example.estoque.repositories.ExpenseRepository;
import com.example.estoque.services.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService){
        this.expenseService = expenseService;
    }

    //GET all expenses
    @GetMapping("/see")
    public ResponseEntity<List<ExpenseResponseDto>> getAllExpenses(){
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    //GET expenses by month
    @GetMapping("/see/month")
    public ResponseEntity<List<ExpenseResponseDto>> getExpenseByMonth(@RequestParam int month, @RequestParam int year){
        List<ExpenseResponseDto> dto = expenseService.getExpenseByMonth(month, year);
        return ResponseEntity.ok(dto);
    }

    //POST expense
    @PostMapping("/register")
    public ResponseEntity<ExpenseResponseDto> registerExpense(@RequestBody ExpenseRequestDto dto){
        ExpenseResponseDto created = expenseService.registerExpense(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //PUT expense
    @PutMapping("/update/{codexp}")
    public ResponseEntity<ExpenseResponseDto> updateExpense(@PathVariable Long codexp, @RequestBody ExpenseRequestDto dto){
        ExpenseResponseDto updated = expenseService.updateExpense(codexp, dto);
        return ResponseEntity.ok(updated);
    }

    //DELETE expense
    @DeleteMapping("/delete/{codexp}")
    public ResponseEntity<ExpenseResponseDto> deleteExpense(@PathVariable Long codexp){
        ExpenseResponseDto deleted = expenseService.deleteExpense(codexp);
        return ResponseEntity.ok(deleted);
    }
}
