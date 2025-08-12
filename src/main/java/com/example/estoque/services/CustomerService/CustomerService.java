package com.example.estoque.services.CustomerService;

import com.example.estoque.dtos.customerDtos.CustomerRequestDto;
import com.example.estoque.dtos.customerDtos.CustomerResponseDto;
import com.example.estoque.entities.customerEntities.BrazilianState;
import com.example.estoque.entities.customerEntities.Customer;
import com.example.estoque.entities.customerEntities.CustomerSpecification;
import com.example.estoque.exceptions.AppException;
import com.example.estoque.mapper.CustomerMapper;
import com.example.estoque.repositories.CustomerRepositories.CustomerRepository;
import com.example.estoque.services.AuditLogService;
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

    private final AuditLogService auditLogService;

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

        List<Customer> customers = customerRepository.findAll(spec);

        if (customers.isEmpty()) {
            throw new AppException("No customers found with the provided filters.", HttpStatus.NOT_FOUND);
        }

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

        // Log the creation of the customer
        auditLogService.log(
                "Customer",
                savedCustomer.getCodcus(),
                "CREATE",
                null,
                null,
                null,
                savedCustomer.getCreatedBy()
        );

        return customerMapper.toDto(savedCustomer);
    }

    //PUT customer logic
    public CustomerResponseDto updateCustomer(Long codcus ,CustomerRequestDto dto) {
        Customer customer = customerRepository.findById(codcus)
                .orElseThrow(() -> new AppException("Customer not found or disabled.", HttpStatus.NOT_FOUND));

        // Capture old values for audit logging
        String oldCusname = customer.getCusname();
        String oldCusaddr = customer.getCusaddr();
        String oldCuscity = customer.getCuscity();
        BrazilianState oldCusstate = customer.getCusstate();
        String oldCuszip = customer.getCuszip();
        String oldCusphone = customer.getCusphone();
        String oldCusemail = customer.getCusemail();


        customer.setCusname(dto.getCusname());
        customer.setCusaddr(dto.getCusaddr());
        customer.setCuscity(dto.getCuscity());
        customer.setCusstate(dto.getCusstate());
        customer.setCuszip(dto.getCuszip());
        customer.setCusphone(dto.getCusphone());
        customer.setCusemail(dto.getCusemail());

        Customer updatedCustomer = customerRepository.save(customer);

        String actor = updatedCustomer.getUpdatedBy();

        // Log the update of the customer

        // Customer Name
        if (!oldCusname.equals(updatedCustomer.getCusname())) {
            auditLogService.log("Customer", updatedCustomer.getCodcus(), "UPDATE",
                    "cusname", oldCusname, updatedCustomer.getCusname(), actor);
        }

        // Address
        if (!oldCusaddr.equals(updatedCustomer.getCusaddr())) {
            auditLogService.log("Customer", updatedCustomer.getCodcus(), "UPDATE",
                    "cusaddr", oldCusaddr, updatedCustomer.getCusaddr(), actor);
        }

        // City
        if (!oldCuscity.equals(updatedCustomer.getCuscity())) {
            auditLogService.log("Customer", updatedCustomer.getCodcus(), "UPDATE",
                    "cuscity", oldCuscity, updatedCustomer.getCuscity(), actor);
        }

        // State
        if (!oldCusstate.equals(updatedCustomer.getCusstate())) {
            auditLogService.log("Customer", updatedCustomer.getCodcus(), "UPDATE",
                    "cusstate", oldCusstate.name(), updatedCustomer.getCusstate().name(), actor);
        }

        // Zip Code
        if (!oldCuszip.equals(updatedCustomer.getCuszip())) {
            auditLogService.log("Customer", updatedCustomer.getCodcus(), "UPDATE",
                    "cuszip", oldCuszip, updatedCustomer.getCuszip(), actor);
        }

        // Phone
        if (!oldCusphone.equals(updatedCustomer.getCusphone())) {
            auditLogService.log("Customer", updatedCustomer.getCodcus(), "UPDATE",
                    "cusphone", oldCusphone, updatedCustomer.getCusphone(), actor);
        }

        // Email
        if (!oldCusemail.equals(updatedCustomer.getCusemail())) {
            auditLogService.log("Customer", updatedCustomer.getCodcus(), "UPDATE",
                    "cusemail", oldCusemail, updatedCustomer.getCusemail(), actor);
        }

        return customerMapper.toDto(updatedCustomer);
    }

    //DELETE customer logic
    public CustomerResponseDto deleteCustomer(Long codcus) {
        Customer customer = customerRepository.findBycodcusAndIsDeletedFalse(codcus)
                .orElseThrow(() -> new AppException("Customer not found or already disabled.", HttpStatus.NOT_FOUND));

        customer.setIsDeleted(true);
        Customer deletedCustomer = customerRepository.save(customer);

        // Log the deletion of the customer
        auditLogService.log(
                "Customer",
                deletedCustomer.getCodcus(),
                "DELETE",
                null,
                null,
                null,
                deletedCustomer.getUpdatedBy()
        );

        return customerMapper.toDto(deletedCustomer);
    }

}
