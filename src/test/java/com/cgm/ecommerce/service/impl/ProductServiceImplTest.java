package com.cgm.ecommerce.service.impl;

import com.cgm.ecommerce.domain.Product;
import com.cgm.ecommerce.dto.ProductDto;
import com.cgm.ecommerce.mapper.ProductMapper;
import com.cgm.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    void testCreateProduct() {
        ProductDto dto = new ProductDto(null, "Test", "Desc", new BigDecimal("9.99"), "tenant1");
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

        Mockito.when(repository.findByIdAndTenantId(1L, "tenant1")).thenReturn(Optional.of(entity));

        ProductDto dto = service.getProductById(1L, "tenant1");

        Assertions.assertEquals("Test", dto.getName());
    }

    @Test
    void testGetProductById_NotFound() {
        Mockito.when(repository.findByIdAndTenantId(1L, "tenant1")).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> service.getProductById(1L, "tenant1"));
    }

    @Test
    void testUpdateProduct_success() {
        // Arrange
        Long productId = 1L;
        String tenantId = "tenant1";

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setTenantId(tenantId);
        existingProduct.setName("Old Name");
        existingProduct.setDescription("Old Description");
        existingProduct.setPrice(BigDecimal.valueOf(50));

        ProductDto updateDto = new ProductDto();
        updateDto.setId(productId);
        updateDto.setTenantId(tenantId);
        updateDto.setName("New Name");
        updateDto.setDescription("New Description");
        updateDto.setPrice(BigDecimal.valueOf(100));

        Mockito.when(repository.findByIdAndTenantId(productId, tenantId)).thenReturn(Optional.of(existingProduct));
        Mockito.when(repository.save(Mockito.any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ProductDto result = service.updateProduct(productId, updateDto);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("New Name", result.getName());
        Assertions.assertEquals("New Description", result.getDescription());
        Assertions.assertEquals(BigDecimal.valueOf(100), result.getPrice());
        Assertions.assertEquals(tenantId, result.getTenantId());

        Mockito.verify(repository).findByIdAndTenantId(productId, tenantId);
        Mockito.verify(repository).save(existingProduct);
    }

    @Test
    void testUpdateProduct_nullDto_throwsException() {
        // Arrange
        Long productId = 1L;

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.updateProduct(productId, null));

        Mockito.verify(repository, Mockito.never()).findByIdAndTenantId(Mockito.anyLong(), Mockito.anyString());
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Product.class));
    }

    @Test
    void testUpdateProduct_productNotFound_throwsException() {
        // Arrange
        Long productId = 1L;
        String tenantId = "tenant1";

        ProductDto updateDto = new ProductDto();
        updateDto.setId(productId);
        updateDto.setTenantId(tenantId);
        updateDto.setName("New Name");

        Mockito.when(repository.findByIdAndTenantId(productId, tenantId)).thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(NoSuchElementException.class, () -> service.updateProduct(productId, updateDto));

        Mockito.verify(repository).findByIdAndTenantId(productId, tenantId);
        Mockito.verify(repository, Mockito.never()).save(Mockito.any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        Product entity = new Product();
        entity.setId(1L);
        entity.setName("Test");

        Mockito.when(repository.existsByIdAndTenantId(1L, "tenant1")).thenReturn(true);

        service.deleteProduct(1L, "tenant1");
        Mockito.verify(repository, Mockito.times(1)).deleteById(1L);
    }
}
