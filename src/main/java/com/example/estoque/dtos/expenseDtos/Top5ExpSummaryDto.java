package com.example.estoque.dtos.expenseDtos;

import java.time.LocalDate;

public interface Top5ExpSummaryDto {
    Long getCodexp();
    String getExpdesc();
    Long getTotalInCents();
    String getExptype();
    LocalDate getExpdate();
    LocalDate getExpdatepay();
}
