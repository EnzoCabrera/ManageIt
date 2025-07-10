package com.example.estoque.mapper;

import com.example.estoque.dtos.customerDtos.CustomerResponseDto;
import com.example.estoque.entities.customerEntities.Customer;
import com.example.estoque.repositories.CustomerRepositories.CustomerRepository;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    private final CustomerRepository customerRepository;

    public CustomerMapper(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerResponseDto toDto(Customer customer){
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setCodcus(customer.getCodcus());
        dto.setCusname(customer.getCusname());
        dto.setCusaddr(customer.getCusaddr());
        dto.setCuscity(customer.getCuscity());
        dto.setCusstate(customer.getCusstate().toString());
        dto.setCuszip(customer.getCuszip());
        dto.setCusphone(customer.getCusphone());
        dto.setCusemail(customer.getCusemail());
        dto.setUpdatedBy(customer.getUpdatedBy());
        dto.setUpdatedAt(customer.getUpdatedAt().toString());
        return dto;
    }
}
