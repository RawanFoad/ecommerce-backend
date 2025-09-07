package com.cgm.ecommerce.service;

import com.cgm.ecommerce.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto product);

    public List<ProductDto> getAllProducts(String tenantId);

    public ProductDto getProductById(Long id, String tenantId);

    ProductDto updateProduct(Long id, ProductDto updatedProduct);

    void deleteProduct(Long id);
}
