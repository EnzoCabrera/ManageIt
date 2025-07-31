package com.example.estoque.dtos.expenseDtos.DashboardDtos;

public interface MonthlyExpSummaryDto {
    Integer getYear();
    Integer getMonth();
    Long getTotalInCents();
}
