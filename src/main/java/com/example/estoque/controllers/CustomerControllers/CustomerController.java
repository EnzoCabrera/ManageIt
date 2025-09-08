package com.example.estoque.controllers.CustomerControllers;

import com.example.estoque.config.Pageable.AllowedSort;
import com.example.estoque.dtos.authDtos.PageResponseDto;
import com.example.estoque.dtos.customerDtos.CustomerRequestDto;
import com.example.estoque.dtos.customerDtos.CustomerResponseDto;
import com.example.estoque.services.CustomerService.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@Tag(name = "Customer")
@SecurityRequirement(name = "BearerAuth")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    //GET customers
    @GetMapping("/see")
    public ResponseEntity<PageResponseDto<CustomerResponseDto>> getCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String zipCode,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @AllowedSort(value = {"name", "address", "city", "state", "zipCode", "phone", "email", "createdAt", "updatedAt"},
                    defaultProp = "createdAt", defaultDir = "DESC")
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return ResponseEntity.ok(customerService.getCustomersSlim(name, address, city, state, zipCode, phone, email, pageable));
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

