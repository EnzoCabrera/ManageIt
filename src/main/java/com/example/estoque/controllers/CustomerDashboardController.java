package com.example.estoque.controllers;

import com.example.estoque.services.CustomerService.CustomerDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
