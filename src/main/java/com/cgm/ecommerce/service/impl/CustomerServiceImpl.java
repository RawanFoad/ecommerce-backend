package com.cgm.ecommerce.service.impl;

import com.cgm.ecommerce.domain.Customer;
import com.cgm.ecommerce.dto.CustomerDto;
import com.cgm.ecommerce.mapper.CustomerMapper;
import com.cgm.ecommerce.repository.CustomerRepository;
import com.cgm.ecommerce.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        if (dto == null) throw new IllegalArgumentException("Customer cannot be null");
        return CustomerMapper.toDto(repository.save(CustomerMapper.toEntity(dto)));
    }

    @Override
    public List<CustomerDto> getAllCustomers(String tenantId) {
        return repository.findByTenantId(tenantId).stream().map(CustomerMapper::toDto).toList();
    }

    @Override
    public CustomerDto getCustomerById(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return CustomerMapper.toDto(customer);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerDto dto) {
        Customer existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        existing.setName(dto.getName());
        existing.setEmail(dto.getEmail());
        return CustomerMapper.toDto(repository.save(existing));
    }

    @Override
    public void deleteCustomer(Long id) {
        if (!repository.existsById(id)) throw new RuntimeException("Customer not found");
        repository.deleteById(id);
    }
}
