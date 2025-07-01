package com.example.estoque.services;

import com.example.estoque.dtos.customerDtos.CustomerRequestDto;
import com.example.estoque.dtos.customerDtos.CustomerResponseDto;
import com.example.estoque.entities.customerEntities.Customer;
import com.example.estoque.entities.customerEntities.CustomerSpecification;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.CustomerMapper;
import com.example.estoque.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;


    //GET customers logic
    public List<CustomerResponseDto> getFilterCustomers(
            String name,
            String address,
            String city,
            String state,
            String zipCode,
            String phone,
            String email
        ) {
        Specification<Customer> spec = Specification.where(CustomerSpecification.isNotDeleted())
                .and(CustomerSpecification.hasName(name))
                .and(CustomerSpecification.hasAddress(address))
                .and(CustomerSpecification.hasCity(city))
                .and(CustomerSpecification.hasState(state))
                .and(CustomerSpecification.hasZipCode(zipCode))
                .and(CustomerSpecification.hasPhone(phone))
                .and(CustomerSpecification.hasEmail(email));

        return customerRepository.findAll(spec)
                .stream()
                .map(customerMapper::toDto)
                .toList();
    }

    //POST customer logic
    public CustomerResponseDto registerCustomer(CustomerRequestDto dto) {
        Customer customer = new Customer();
        customer.setCusname(dto.getCusname());
        customer.setCusaddr(dto.getCusaddr());
        customer.setCuscity(dto.getCuscity());
        customer.setCusstate(dto.getCusstate());
        customer.setCuszip(dto.getCuszip());
        customer.setCusphone(dto.getCusphone());
        customer.setCusemail(dto.getCusemail());

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    //PUT customer logic
    public CustomerResponseDto updateCustomer(Long codcus ,CustomerRequestDto dto) {
        Customer customer = customerRepository.findById(codcus)
                .orElseThrow(() -> new AppException("Customer not found or disabled.", HttpStatus.NOT_FOUND));

        customer.setCusname(dto.getCusname());
        customer.setCusaddr(dto.getCusaddr());
        customer.setCuscity(dto.getCuscity());
        customer.setCusstate(dto.getCusstate());
        customer.setCuszip(dto.getCuszip());
        customer.setCusphone(dto.getCusphone());
        customer.setCusemail(dto.getCusemail());

        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(updatedCustomer);
    }

    //DELETE customer logic
    public CustomerResponseDto deleteCustomer(Long codcus) {
        Customer customer = customerRepository.findBycodcusAndIsDeletedFalse(codcus)
                .orElseThrow(() -> new AppException("Customer not found or already disabled.", HttpStatus.NOT_FOUND));

        customer.setIsDeleted(true);
        Customer deletedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(deletedCustomer);
    }

}
