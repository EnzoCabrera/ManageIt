package com.example.estoque.controllers.CustomerControllers;

import com.example.estoque.dtos.customerDtos.customerDashboardsDtos.NewCusPerMonthDto;
import com.example.estoque.dtos.customerDtos.customerDashboardsDtos.Top5CusHighestAmountSpent;
import com.example.estoque.dtos.customerDtos.customerDashboardsDtos.Top5CusMoreOrdDto;
import com.example.estoque.services.CustomerService.CustomerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerDashboardController {

    private final CustomerDashboardService customerDashboardService;

    //GET active customers
    @GetMapping("/see/active-customers")
    public ResponseEntity<Integer> getActiveCustomers() {
        return ResponseEntity.ok(customerDashboardService.getActiveCustomers());
    }

    //GET new customers per month
    @GetMapping("/see/new-customers-per-month")
    public ResponseEntity<List<NewCusPerMonthDto>> getNewCustomersPerMonth() {
        return ResponseEntity.ok(customerDashboardService.getNewCustomersPerMonth());
    }

    //GET top 5 customers with most orders
    @GetMapping("/see/top5-customers-with-most-orders")
    public ResponseEntity<List<Top5CusMoreOrdDto>> getTop5CusWithMostOrds() {
        return ResponseEntity.ok(customerDashboardService.getTop5CusWithMostOrds());
    }

    //GET top 5 customers with highest amount spent
    @GetMapping("/see/top5-customers-highest-amount-spent")
    public ResponseEntity<List<Top5CusHighestAmountSpent>> getTop5CusHighestAmountSpent() {
        return ResponseEntity.ok(customerDashboardService.getTop5CusHighestAmountSpent());
    }
}
