package com.example.estoque.dtos.customerDtos.customerDashboardsDtos;

import java.time.LocalDate;

public interface InactiveCustomerDto {
    Long getCodcus();
    String getCusname();
    String getCusemail();
    LocalDate lastOrderDate();
}
