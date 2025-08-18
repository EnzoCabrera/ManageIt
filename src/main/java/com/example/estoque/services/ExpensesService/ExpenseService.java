package com.example.estoque.services.ExpensesService;

import com.example.estoque.dtos.authDtos.PageResponseDto;
import com.example.estoque.dtos.expenseDtos.*;
import com.example.estoque.entities.expenseEntities.Expense;
import com.example.estoque.entities.expenseEntities.ExpenseSpecification;
import com.example.estoque.entities.expenseEntities.ExpenseStatus;
import com.example.estoque.entities.expenseEntities.ExpenseType;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.ExpenseMapper;
import com.example.estoque.repositories.ExpenseRepositories.ExpenseRepository;
import com.example.estoque.services.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Autowired
    private ExpenseMapper expenseMapper;
    @Autowired
    private AuditLogService auditLogService;


    //GET expenses logic
    public PageResponseDto<ExpenseResponseDto> getExpensesSlim(
            String type,
            Integer month,
            Integer year,
            Integer payMonth,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate startDatePay,
            LocalDate endDatePay,
            String expSts,
            Pageable pageable
    ) {

        Specification<Expense> spec = Specification.where(ExpenseSpecification.isNotDeleted())
                .and(ExpenseSpecification.hasType(type))
                .and(ExpenseSpecification.isBetweenExpdate(startDate, endDate))
                .and(ExpenseSpecification.isBetweenExpdatepay(startDatePay, endDatePay))
                .and(ExpenseSpecification.hasExpSts(expSts));

        Page<Expense> page = expenseRepository.findAll(spec, pageable);

        List<ExpenseResponseDto> content = page.map(expenseMapper::toDto).getContent();

        if (page.isEmpty()) {
            throw new AppException("No users found matching the given filters.", HttpStatus.NOT_FOUND);
        }

        return new PageResponseDto<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
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

        // Log the creation of the expense
        auditLogService.log(
                "Expense",
                savedExpense.getCodexp(),
                "CREATE",
                null,
                null,
                null,
                savedExpense.getCreatedBy()
        );

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
                dto.getExpdatepay().isBefore(LocalDate.now())) {
            throw new AppException("Cannot create an expense with status PENDING when payment date is in the past. Use OVERDUE, PAID or CANCELLED instead.", HttpStatus.BAD_REQUEST);
        }

        // Capture old values for audit logging
        String oldExpdesc = expense.getExpdesc();
        Integer oldExpCostInCents = expense.getExpCostInCents();
        LocalDate oldExpdate = expense.getExpdate();
        LocalDate oldExpdatepay = expense.getExpdatepay();
        ExpenseStatus oldExpsts = expense.getExpsts();
        ExpenseType oldExptype = expense.getExptype();


        expense.setExpdesc(dto.getExpdesc());
        expense.setExpCostInCents(dto.getExpCostInCents());
        expense.setExpdate(dto.getExpdate());
        expense.setExpdatepay(dto.getExpdatepay());
        expense.setExptype(dto.getExptype());
        expense.setExpsts(dto.getExpsts());

        Expense updatedExpense = expenseRepository.save(expense);

        String actor = expense.getUpdatedBy();

        // Log the update of the expense

        // Expense Description
        if (!oldExpdesc.equals(updatedExpense.getExpdesc())) {
            auditLogService.log("Expense", updatedExpense.getCodexp(), "UPDATE",
                    "expdesc", oldExpdesc, updatedExpense.getExpdesc(), actor);
        }

        // Expense Cost
        if (!oldExpCostInCents.equals(updatedExpense.getExpCostInCents())) {
            auditLogService.log("Expense", updatedExpense.getCodexp(), "UPDATE",
                    "expCostInCents", oldExpCostInCents.toString(), updatedExpense.getExpCostInCents().toString(), actor);
        }

        // Expense Date
        if (!oldExpdate.equals(updatedExpense.getExpdate())) {
            auditLogService.log("Expense", updatedExpense.getCodexp(), "UPDATE",
                    "expdate", oldExpdate.toString(), updatedExpense.getExpdate().toString(), actor);
        }

        // Expense Payment Date
        if (!oldExpdatepay.equals(updatedExpense.getExpdatepay())) {
            auditLogService.log("Expense", updatedExpense.getCodexp(), "UPDATE",
                    "expdatepay", oldExpdatepay.toString(), updatedExpense.getExpdatepay().toString(), actor);
        }

        // Expense Type
        if (!oldExptype.equals(updatedExpense.getExptype())) {
            auditLogService.log("Expense", updatedExpense.getCodexp(), "UPDATE",
                    "exptype", oldExptype.toString(), updatedExpense.getExptype().toString(), actor);
        }

        // Expense Status
        if (!oldExpsts.equals(updatedExpense.getExpsts())) {
            auditLogService.log("Expense", updatedExpense.getCodexp(), "UPDATE",
                    "expsts", oldExpsts.toString(), updatedExpense.getExpsts().toString(), actor);
        }


        return expenseMapper.toDto(updatedExpense);
    }

    //DELETE expense logic
    public ExpenseResponseDto deleteExpense(Long codexp){
        Expense expense = expenseRepository.findBycodexpAndIsDeletedFalse(codexp)
                .orElseThrow(() -> new AppException("Expense not found or already deleted.", HttpStatus.NOT_FOUND));

        expense.setIsDeleted(true);
        Expense deletedExpense = expenseRepository.save(expense);

        // Log the deletion of the expense
        auditLogService.log(
                "Expense",
                deletedExpense.getCodexp(),
                "DELETE",
                null,
                null,
                null,
                deletedExpense.getUpdatedBy()
        );

        return expenseMapper.toDto(deletedExpense);
    }

}
