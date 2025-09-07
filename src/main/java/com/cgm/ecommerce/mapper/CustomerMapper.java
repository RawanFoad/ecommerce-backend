package com.cgm.ecommerce.mapper;

import com.cgm.ecommerce.domain.Customer;
import com.cgm.ecommerce.dto.CustomerDto;

public class CustomerMapper {
    public static CustomerDto toDto(Customer customer) {
        if (customer == null) return null;
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setName(customer.getName());
        dto.setEmail(customer.getEmail());
        return dto;
    }

    public static Customer toEntity(CustomerDto dto) {
        if (dto == null) return null;
        Customer c = new Customer();
        c.setName(dto.getName());
        c.setEmail(dto.getEmail());
        return c;
    }
}
