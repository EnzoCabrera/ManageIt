package com.example.estoque.controllers;

import com.example.estoque.dtos.customerDtos.CustomerRequestDto;
import com.example.estoque.dtos.customerDtos.CustomerResponseDto;
import com.example.estoque.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //GET all customers
    @GetMapping("/see")
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers(){
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    //GET customer by ID
    @GetMapping("/see/{codcus}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long codcus){
        CustomerResponseDto dto = customerService.getByCodcus(codcus);
        return ResponseEntity.ok(dto);
    }

    //POST all customers
    @PostMapping("/register")
    public ResponseEntity<CustomerResponseDto> RegisterCustomer(@RequestBody CustomerRequestDto dto){
        CustomerResponseDto created = customerService.registerCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //PUT all customers
    @PutMapping("/update/{codcus}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable Long codcus, @RequestBody CustomerRequestDto dto){
        CustomerResponseDto updated = customerService.updateCustomer(codcus, dto);
        return ResponseEntity.ok(updated);
    }

    //DELETE all customers
    @DeleteMapping("/delete/{codcus}")
    public ResponseEntity<CustomerResponseDto> deleteCustomer(@PathVariable Long codcus){
        CustomerResponseDto deleted = customerService.deleteCustomer(codcus);
        return ResponseEntity.ok(deleted);
    }
}

