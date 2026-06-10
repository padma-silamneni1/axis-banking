package com.axisbanking.accounts.service.impl;

import com.axisbanking.accounts.dto.CustomerDto;
import com.axisbanking.accounts.exception.CustomerAlreadyExistsException;
import com.axisbanking.accounts.exception.ResourceNotFoundException;
import com.axisbanking.accounts.model.Customer;
import com.axisbanking.accounts.repository.CustomerRepository;
import com.axisbanking.accounts.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        if (customerRepository.existsByMobileNumber(dto.getMobileNumber())) {
            throw new CustomerAlreadyExistsException(
                    "Customer already registered with mobile number: " + dto.getMobileNumber());
        }
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new CustomerAlreadyExistsException(
                    "Customer already registered with email: " + dto.getEmail());
        }

        Customer customer = mapToEntity(dto);
        Customer saved = customerRepository.save(customer);
        return mapToDto(saved);
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id.toString()));
        return mapToDto(customer);
    }

    @Override
    public CustomerDto getCustomerByMobileNumber(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));
        return mapToDto(customer);
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id.toString()));

        customer.setName(dto.getName());
        customer.setEmail(dto.getEmail());
        customer.setMobileNumber(dto.getMobileNumber());
        customer.setPanNumber(dto.getPanNumber());
        customer.setAadhaarNumber(dto.getAadhaarNumber());
        customer.setAddress(dto.getAddress());
        customer.setDateOfBirth(dto.getDateOfBirth());

        Customer updated = customerRepository.save(customer);
        return mapToDto(updated);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", id.toString()));
        customerRepository.delete(customer);
    }

    private CustomerDto mapToDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .mobileNumber(customer.getMobileNumber())
                .panNumber(customer.getPanNumber())
                .aadhaarNumber(customer.getAadhaarNumber())
                .address(customer.getAddress())
                .dateOfBirth(customer.getDateOfBirth())
                .build();
    }

    private Customer mapToEntity(CustomerDto dto) {
        return Customer.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .mobileNumber(dto.getMobileNumber())
                .panNumber(dto.getPanNumber())
                .aadhaarNumber(dto.getAadhaarNumber())
                .address(dto.getAddress())
                .dateOfBirth(dto.getDateOfBirth())
                .build();
    }
}
