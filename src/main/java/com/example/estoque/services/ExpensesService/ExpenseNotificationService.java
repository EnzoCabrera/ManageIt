package com.example.estoque.services.ExpensesService;

import com.example.estoque.entities.expenseEntities.Expense;
import com.example.estoque.entities.expenseEntities.ExpenseStatus;
import com.example.estoque.repositories.ExpenseRepository;
import com.example.estoque.services.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ExpenseNotificationService {

    private final ExpenseRepository expenseRepository;

    private final EmailService emailService;

    public ExpenseNotificationService(ExpenseRepository expenseRepository, EmailService emailService) {
        this.expenseRepository = expenseRepository;
        this.emailService = emailService;
    }

    //Email notification for expenses that are about to become overdue
    @Scheduled(cron = "0 0 7 * * *")
    public void notifyUpcomingOverdueExpenses() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Expense> expenses = expenseRepository.findByExpstsAndExpdatepay(ExpenseStatus.PENDING, tomorrow);

        expenses.forEach(expense -> {
                    String userEmail = expense.getCreatedBy();
                    String subject = "Upcoming Overdue Expense";
                    String body = String.format(
                            "Hi!\n\nThis is a reminder that your expense \"%s\" (due date: %s) is about to become OVERDUE.\n" +
                                    "To avoid any penalties or disruptions, please pay this expense by tomorrow.\n\n" +
                                    "Thanks for using ManageIt!",
                            expense.getExpdesc(),
                            expense.getExpdatepay()
                    );

                    emailService.sendSimpleMessage(userEmail, subject, body);

                });
    }

    //Email notification for overdue expenses
    @Scheduled(cron = "0 0 */6 * * *")
    public void notifyOverdueExpenses() {
        List<Expense> overdueExpenses = expenseRepository.findByExpstsAndIsDeletedFalse(ExpenseStatus.OVERDUE);

        overdueExpenses.forEach(expense -> {
            String userEmail = expense.getCreatedBy();
            String subject = "Upcoming Overdue Expense";
            String body = String.format(
                    "Hi!\n\nThis is a reminder that your expense \"%s\" (due date: %s) is OVERDUE.\n" +
                            "Please pay this expense as soon as possible to avoid further penalties or disruptions.\n\n" +
                            "Thanks for using ManageIt!",
                    expense.getExpdesc(),
                    expense.getExpdatepay()
            );

            emailService.sendSimpleMessage(userEmail, subject, body);

        });
    }
}
