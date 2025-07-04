package com.example.estoque.dtos.expenseDtos;

import java.time.LocalDate;

public interface FutureExpenseSummaryDto {
    Long getTotalInCents();
    Integer getCount();
    String getExptype();
}
