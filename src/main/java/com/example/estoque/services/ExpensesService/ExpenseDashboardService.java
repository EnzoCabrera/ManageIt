package com.example.estoque.services.ExpensesService;

import com.example.estoque.dtos.expenseDtos.DashboardDtos.*;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.repositories.ExpenseRepositories.ExpenseDashboardRepository;
import com.example.estoque.repositories.ExpenseRepositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseDashboardService {

    private final ExpenseDashboardRepository expenseDashboardRepository;

    //GET current month expenses logic
    public List<MonthlyExpSummaryDto> getMonthlyExpenseSummary() {
        if (expenseDashboardRepository.findMonthlyExpenseSummary().isEmpty()) {
            throw new AppException("There are no expense this month", HttpStatus.NOT_FOUND);
        }
        return expenseDashboardRepository.findMonthlyExpenseSummary();
    }

    //GET current month expenses by type logic
    public List<ExpTypeSummaryDto> getExpTypeSummary() {
        if (expenseDashboardRepository.getExpTypeSummary().isEmpty()) {
            throw new AppException("There are no expense this month", HttpStatus.NOT_FOUND);
        }
        return expenseDashboardRepository.getExpTypeSummary();
    }

    //GET current month top 5 expenses logic
    public List<Top5ExpSummaryDto> getTop5ExpenseSummary() {
        if (expenseDashboardRepository.getTop5ExpenseSummary().isEmpty()) {
            throw new AppException("There are no expense this month", HttpStatus.NOT_FOUND);
        }
        return expenseDashboardRepository.getTop5ExpenseSummary();
    }

    //GET current month expenses by status logic
    public List<ExpSummaryByStsDto> getExpSummaryBySts() {
        if (expenseDashboardRepository.getExpSummaryBySts().isEmpty()) {
            throw new AppException("There are no expense this month", HttpStatus.NOT_FOUND);
        }
        return expenseDashboardRepository.getExpSummaryBySts();
    }

    //GET current month expenses unpaid logic
    public List<FutureExpenseSummaryDto> getFutureExpensesGrouped() {
        if (expenseDashboardRepository.getFutureExpensesGrouped().isEmpty()) {
            throw new AppException("There are no expense this month", HttpStatus.NOT_FOUND);
        }
        return expenseDashboardRepository.getFutureExpensesGrouped();
    }
}
