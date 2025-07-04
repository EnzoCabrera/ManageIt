package com.example.estoque.dtos.expenseDtos;

public interface MonthlyExpSummaryDto {
    Integer getYear();
    Integer getMonth();
    Long getTotalInCents();
}
