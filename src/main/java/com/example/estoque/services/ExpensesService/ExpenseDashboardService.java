package com.example.estoque.services.ExpensesService;

import com.example.estoque.dtos.expenseDtos.DashboardDtos.*;
import com.example.estoque.repositories.ExpenseRepositories.ExpenseDashboardRepository;
import com.example.estoque.repositories.ExpenseRepositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseDashboardService {

    private final ExpenseDashboardRepository expenseDashboardRepository;

    //GET current month expenses logic
    public List<MonthlyExpSummaryDto> getMonthlyExpenseSummary() {
        return expenseDashboardRepository.findMonthlyExpenseSummary();
    }

    //GET current month expenses by type logic
    public List<ExpTypeSummaryDto> getExpTypeSummary() {
        return expenseDashboardRepository.getExpTypeSummary();
    }

    //GET current month top 5 expenses logic
    public List<Top5ExpSummaryDto> getTop5ExpenseSummary() {
        return expenseDashboardRepository.getTop5ExpenseSummary();
    }

    //GET current month expenses by status logic
    public List<ExpSummaryByStsDto> getExpSummaryBySts() {
        return expenseDashboardRepository.getExpSummaryBySts();
    }

    //GET current month expenses unpaid logic
    public List<FutureExpenseSummaryDto> getFutureExpensesGrouped() {
        return expenseDashboardRepository.getFutureExpensesGrouped();
    }
}
