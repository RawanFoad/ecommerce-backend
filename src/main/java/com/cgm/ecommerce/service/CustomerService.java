package com.cgm.ecommerce.service;

import com.cgm.ecommerce.dto.CustomerDto;

import java.util.List;

public interface CustomerService {
    CustomerDto createCustomer(CustomerDto dto);
    List<CustomerDto> getAllCustomers(String tenantId);
    CustomerDto getCustomerById(Long id);
    CustomerDto updateCustomer(Long id, CustomerDto dto);
    void deleteCustomer(Long id);
}
