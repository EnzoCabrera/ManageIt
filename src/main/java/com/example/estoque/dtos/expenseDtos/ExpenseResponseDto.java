package com.example.estoque.dtos.expenseDtos;

import com.example.estoque.entities.expenseEntities.ExpenseStatus;
import com.example.estoque.entities.expenseEntities.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExpenseResponseDto {

    @NotNull
    private Long codexp;

    @NotNull
    private String expdesc;

    @NotNull
    private Integer expCostInCents;

    @NotNull
    private LocalDate expdate;

    @NotNull
    private LocalDate expdatepay;

    @NotNull
    private ExpenseType exptype;

    @NotNull
    private ExpenseStatus expsts;
}
