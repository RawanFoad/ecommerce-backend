package com.cgm.ecommerce.service.impl;

import com.cgm.ecommerce.domain.Customer;
import com.cgm.ecommerce.dto.CustomerDto;
import com.cgm.ecommerce.mapper.CustomerMapper;
import com.cgm.ecommerce.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;

    @Test
    void testCreateCustomer() {
        CustomerDto dto = new CustomerDto();
        dto.setName("John");
        dto.setEmail("john@example.com");

        Customer entity = CustomerMapper.toEntity(dto);
        entity.setId(1L);

        when(repository.save(any(Customer.class))).thenReturn(entity);

        CustomerDto saved = service.createCustomer(dto);

        assertNotNull(saved.getId());
        assertEquals("John", saved.getName());
        assertEquals("john@example.com", saved.getEmail());
    }

    @Test
    void testCreateCustomer_NullDtoThrows() {
        assertThrows(IllegalArgumentException.class, () -> service.createCustomer(null));
    }

    @Test
    void testGetAllCustomers() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setTenantId("tenant1");
        customer.setName("Jane");

        when(repository.findByTenantId("tenant1")).thenReturn(List.of(customer));

        List<CustomerDto> customers = service.getAllCustomers("tenant1");

        assertEquals(1, customers.size());
        assertEquals("Jane", customers.get(0).getName());
    }

    @Test
    void testGetCustomerById_Found() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("Alice");

        when(repository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerDto dto = service.getCustomerById(1L);

        assertEquals("Alice", dto.getName());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.getCustomerById(1L));
    }

    @Test
    void testUpdateCustomer_Found() {
        Customer existing = new Customer();
        existing.setId(1L);
        existing.setName("Old");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        CustomerDto updateDto = new CustomerDto();
        updateDto.setName("New");
        updateDto.setEmail("new@example.com");

        CustomerDto updated = service.updateCustomer(1L, updateDto);

        assertEquals("New", updated.getName());
        assertEquals("new@example.com", updated.getEmail());
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        CustomerDto dto = new CustomerDto();
        dto.setName("X");

        assertThrows(RuntimeException.class, () -> service.updateCustomer(1L, dto));
    }

    @Test
    void testDeleteCustomer_Found() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteCustomer(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> service.deleteCustomer(1L));
    }
}
