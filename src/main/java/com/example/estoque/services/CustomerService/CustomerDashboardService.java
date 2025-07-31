package com.example.estoque.services.CustomerService;

import com.example.estoque.dtos.customerDtos.customerDashboardsDtos.*;
import com.example.estoque.repositories.CustomerRepositories.CustomerDashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerDashboardService {

    private final CustomerDashboardRepository customerDashboardRepository;

    //GET active customers logic
    public Integer getActiveCustomers() {
        return customerDashboardRepository.findAllActiveCus();
    }

    //GET new customers per month logic
    public List<NewCusPerMonthDto> getNewCustomersPerMonth() {
        return customerDashboardRepository.findNewCusPerMonth();
    }

    //GET top 5 customers with most orders logic
    public List<Top5CusMoreOrdDto> getTop5CusWithMostOrds() {
        return customerDashboardRepository.findTop5CusMoreOrd();
    }

    //GET top 5 customers with highest amount spent logic
    public List<Top5CusHighestAmountSpent> getTop5CusHighestAmountSpent() {
        return customerDashboardRepository.findTop5CusHighestAmountSpent();
    }

    //GET inactive customers logic
    public List<InactiveCustomerDto> getInactiveCustomers() {
        return customerDashboardRepository.findInactiveCustomers();
    }
}
