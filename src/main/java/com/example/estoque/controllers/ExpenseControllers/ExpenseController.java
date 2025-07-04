package com.example.estoque.controllers.ExpenseControllers;

import com.example.estoque.dtos.expenseDtos.*;
import com.example.estoque.dtos.expenseDtos.DashboardDtos.*;
import com.example.estoque.services.ExpensesService.ExpenseDashboardService;
import com.example.estoque.services.ExpensesService.ExpenseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService, ExpenseDashboardService expenseDashboardService) {
        this.expenseService = expenseService;
    }

    //GET expenses
    @GetMapping("/see")
    public ResponseEntity<List<ExpenseResponseDto>> getExpenses(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year,
        @RequestParam(required = false) Integer payMonth,
        @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDatePay,
        @RequestParam(required = false)@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDatePay,
        @RequestParam(required = false) String expSts

    ) {
        List<ExpenseResponseDto> expenses = expenseService.getFilterExpenses(type, month, year, payMonth, startDate, endDate, startDatePay, endDatePay, expSts);
        return ResponseEntity.ok(expenses);
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
