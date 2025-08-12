package com.example.estoque.services.CustomerService;

import com.example.estoque.dtos.customerDtos.customerDashboardsDtos.*;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.repositories.CustomerRepositories.CustomerDashboardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        if (customerDashboardRepository.findNewCusPerMonth().isEmpty()) {
            throw new AppException("No new customers found for the current month.", HttpStatus.NOT_FOUND);
        }
        return customerDashboardRepository.findNewCusPerMonth();
    }

    //GET top 5 customers with most orders logic
    public List<Top5CusMoreOrdDto> getTop5CusWithMostOrds() {
        if (customerDashboardRepository.findTop5CusMoreOrd().isEmpty()) {
            throw new AppException("There is no orders for any customer", HttpStatus.NOT_FOUND);
        }
        return customerDashboardRepository.findTop5CusMoreOrd();
    }

    //GET top 5 customers with highest amount spent logic
    public List<Top5CusHighestAmountSpent> getTop5CusHighestAmountSpent() {
        if (customerDashboardRepository.findTop5CusHighestAmountSpent().isEmpty()) {
            throw new AppException("any customer has bought", HttpStatus.NOT_FOUND);
        }
        return customerDashboardRepository.findTop5CusHighestAmountSpent();
    }

    //GET inactive customers logic
    public List<InactiveCustomerDto> getInactiveCustomers() {
        if (customerDashboardRepository.findInactiveCustomers().isEmpty()) {
            throw new AppException("Any customer is inactive", HttpStatus.NOT_FOUND);
        }
        return customerDashboardRepository.findInactiveCustomers();
    }
}
