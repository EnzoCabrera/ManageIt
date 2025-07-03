package com.example.estoque.services.ExpensesService;

import com.example.estoque.entities.expenseEntities.Expense;
import com.example.estoque.entities.expenseEntities.ExpenseStatus;
import com.example.estoque.repositories.ExpenseRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseStatusScheduler {

    private final ExpenseRepository expenseRepository;

    @Scheduled(cron = "0 0 */6 * * *")
    @Transactional
    public void updateOverdueExpenses() {
        LocalDate today = LocalDate.now();
        List<Expense> overdueExpenses = expenseRepository.findByExpstsAndExpdatepayBeforeAndIsDeletedFalse(ExpenseStatus.PENDING, today);

        overdueExpenses.forEach(expense -> expense.setExpsts(ExpenseStatus.OVERDUE));

        expenseRepository.saveAll(overdueExpenses);
    }
}
