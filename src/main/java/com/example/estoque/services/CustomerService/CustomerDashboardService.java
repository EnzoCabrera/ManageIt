package com.example.estoque.services.CustomerService;

import com.example.estoque.dtos.customerDtos.customerDashboardsDtos.ActiveCustomersDto;
import com.example.estoque.repositories.CustomerRepositories.CustomerDashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDashboardService {

    private final CustomerDashboardRepository customerDashboardRepository;

    //GET active customers logic
    public Integer getActiveCustomers() {
        return customerDashboardRepository.findAllActiveCus();
    }
}
