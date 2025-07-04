package com.example.estoque.controllers.ExpenseControllers;

import com.example.estoque.dtos.expenseDtos.DashboardDtos.*;
import com.example.estoque.services.ExpensesService.ExpenseDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseDashboardController {

    private final ExpenseDashboardService expenseDashboardService;

    public ExpenseDashboardController(ExpenseDashboardService expenseDashboardService) {
        this.expenseDashboardService = expenseDashboardService;
    }

    //GET current month expenses
    @GetMapping("/see/monthly-summary")
    public ResponseEntity<List<MonthlyExpSummaryDto>> getMonthlyExpenseSummary() {
        return ResponseEntity.ok(expenseDashboardService.getMonthlyExpenseSummary());
    }

    //GET current month expenses by type
    @GetMapping("see/type-summary")
    public ResponseEntity<List<ExpTypeSummaryDto>> getExpTypeSummary() {
        return ResponseEntity.ok(expenseDashboardService.getExpTypeSummary());
    }

    //GET current month top 5 expenses
    @GetMapping("/see/top5-summary")
    public ResponseEntity<List<Top5ExpSummaryDto>> getTop5ExpenseSummary() {
        return ResponseEntity.ok(expenseDashboardService.getTop5ExpenseSummary());
    }

    //GET current month expenses by status
    @GetMapping("/see/status-summary")
    public ResponseEntity<List<ExpSummaryByStsDto>> getExpStatusSummary() {
        return ResponseEntity.ok(expenseDashboardService.getExpSummaryBySts());
    }

    //GET current month expenses unpaid
    @GetMapping("/see/unpaid-summary")
    public ResponseEntity<List<FutureExpenseSummaryDto>> getNotPaidExpenseSummary() {
        return ResponseEntity.ok(expenseDashboardService.getFutureExpensesGrouped());
    }
}
