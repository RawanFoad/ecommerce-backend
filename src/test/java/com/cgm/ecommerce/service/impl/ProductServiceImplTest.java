package com.cgm.ecommerce.service.impl;

import com.cgm.ecommerce.domain.Product;
import com.cgm.ecommerce.dto.ProductDto;
import com.cgm.ecommerce.mapper.ProductMapper;
import com.cgm.ecommerce.repository.ProductRepository;
import com.cgm.ecommerce.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void testCreateProduct() {
        ProductDto dto = new ProductDto(null, "Test", "Desc", new BigDecimal("9.99"));
        Product entity = ProductMapper.toEntity(dto);
        entity.setId(1L);

        Mockito.when(repository.save(ArgumentMatchers.any(Product.class))).thenReturn(entity);

        ProductDto saved = service.createProduct(dto);

        Assertions.assertNotNull(saved.getId());
        Assertions.assertEquals("Test", saved.getName());
    }

    @Test
    void testGetProductById_Found() {
        Product entity = new Product();
        entity.setId(1L);
        entity.setName("Test");

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(entity));

        ProductDto dto = service.getProductById(1L);

        Assertions.assertEquals("Test", dto.getName());
    }

    @Test
    void testGetProductById_NotFound() {
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> service.getProductById(1L));
    }

    @Test
    void testUpdateProduct() {
        Product entity = new Product();
        entity.setId(1L);
        entity.setName("Old");

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(entity));
        Mockito.when(repository.save(ArgumentMatchers.any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductDto updateDto = new ProductDto(null, "New", "Updated", new BigDecimal("19.99"));

        ProductDto updated = service.updateProduct(1L, updateDto);

        Assertions.assertEquals("New", updated.getName());
    }

    @Test
    void testDeleteProduct() {
        service.deleteProduct(1L);
        Mockito.verify(repository, Mockito.times(1)).deleteById(1L);
    }
}
