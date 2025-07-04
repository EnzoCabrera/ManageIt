package com.example.estoque.services.ExpensesService;

import com.example.estoque.dtos.expenseDtos.*;
import com.example.estoque.entities.expenseEntities.Expense;
import com.example.estoque.entities.expenseEntities.ExpenseSpecification;
import com.example.estoque.entities.expenseEntities.ExpenseStatus;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.ExpenseMapper;
import com.example.estoque.repositories.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseMapper expenseMapper;


    //GET expenses logic
    public List<ExpenseResponseDto> getFilterExpenses(
            String type,
            Integer month,
            Integer year,
            Integer payMonth,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate startDatePay,
            LocalDate endDatePay,
            String expSts
    ) {

        Specification<Expense> spec = Specification.where(ExpenseSpecification.isNotDeleted())
                .and(ExpenseSpecification.hasType(type))
                .and(ExpenseSpecification.isBetweenExpdate(startDate, endDate))
                .and(ExpenseSpecification.isBetweenExpdatepay(startDatePay, endDatePay))
                .and(ExpenseSpecification.hasExpSts(expSts));

        return expenseRepository.findAll(spec)
                .stream()
                .map(expenseMapper::toDto)
                .toList();
    }

    //GET current month expenses logic
    public List<MonthlyExpSummaryDto> getMonthlyExpenseSummary() {
        return expenseRepository.findMonthlyExpenseSummary();
    }

    //GET current month expenses by type logic
    public List<ExpTypeSummaryDto> getExpTypeSummary() {
        return expenseRepository.getExpTypeSummary();
    }

    //GET current month tip 5 expenses logic
    public List<Top5ExpSummaryDto> getTop5ExpenseSummary() {
        return expenseRepository.getTop5ExpenseSummary();
    }

    //GET current month expenses bu status logic
    public List<ExpSummaryByStsDto> getExpSummaryBySts() {
        return expenseRepository.getExpSummaryBySts();
    }

    //POST expense logic
    public ExpenseResponseDto registerExpense(ExpenseRequestDto dto){
        Optional.ofNullable(dto.getExpdatepay())
                .filter(payDate -> dto.getExpdate() != null && !payDate.isBefore(dto.getExpdate()))
                .orElseThrow(() -> new AppException("Payment date cannot be before the expense date.", HttpStatus.BAD_REQUEST));

        if (dto.getExpsts() == ExpenseStatus.PENDING &&
            dto.getExpdatepay().isBefore(LocalDate.now())){
            throw new AppException("Cannot create an expense with status PENDING when payment date is in the past. Use OVERDUE, PAID or CANCELLED instead.", HttpStatus.BAD_REQUEST);
        }

        Expense expense = new Expense();
        expense.setExpdesc(dto.getExpdesc());
        expense.setExpCostInCents(dto.getExpCostInCents());
        expense.setExpdate(dto.getExpdate());
        expense.setExpdatepay(dto.getExpdatepay());
        expense.setExptype(dto.getExptype());
        expense.setExpsts(dto.getExpsts());

        Expense savedExpense = expenseRepository.save(expense);
        return expenseMapper.toDto(savedExpense);
    }

    //PUT expense logic
    public ExpenseResponseDto updateExpense(Long codexp ,ExpenseRequestDto dto){
        Expense expense = expenseRepository.findById(codexp)
                .orElseThrow(() -> new AppException("Expense not found or deleted.", HttpStatus.NOT_FOUND));

        Optional.ofNullable(dto.getExpdatepay())
                .filter(payDate -> dto.getExpdate() != null && !payDate.isBefore(dto.getExpdate()))
                .orElseThrow(() -> new AppException("Payment date cannot be before the expense date.", HttpStatus.BAD_REQUEST));

        if (dto.getExpsts() == ExpenseStatus.PENDING &&
                dto.getExpdatepay().isBefore(LocalDate.now())){
            throw new AppException("Cannot create an expense with status PENDING when payment date is in the past. Use OVERDUE, PAID or CANCELLED instead.", HttpStatus.BAD_REQUEST);
        }

        expense.setExpdesc(dto.getExpdesc());
        expense.setExpCostInCents(dto.getExpCostInCents());
        expense.setExpdate(dto.getExpdate());
        expense.setExpdatepay(dto.getExpdatepay());
        expense.setExptype(dto.getExptype());
        expense.setExpsts(dto.getExpsts());

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
