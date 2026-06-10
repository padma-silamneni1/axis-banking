package com.axisbanking.accounts.service;

import com.axisbanking.accounts.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    CustomerDto createCustomer(CustomerDto customerDto);

    CustomerDto getCustomerById(Long id);

    CustomerDto getCustomerByMobileNumber(String mobileNumber);

    List<CustomerDto> getAllCustomers();

    CustomerDto updateCustomer(Long id, CustomerDto customerDto);

    void deleteCustomer(Long id);
}
