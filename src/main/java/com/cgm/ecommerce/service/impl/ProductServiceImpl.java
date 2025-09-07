package com.cgm.ecommerce.service.impl;

import com.cgm.ecommerce.domain.Product;
import com.cgm.ecommerce.dto.ProductDto;
import com.cgm.ecommerce.mapper.ProductMapper;
import com.cgm.ecommerce.repository.ProductRepository;
import com.cgm.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductDto createProduct(ProductDto dto) {
        if (dto == null) throw new IllegalArgumentException("Product cannot be null");
        Product saved = repository.save(ProductMapper.toEntity(dto));
        return ProductMapper.toDto(saved);
    }

    public List<ProductDto> getAllProducts(String tenantId) {
        List<Product> products = (tenantId == null) ? repository.findAll() : repository.findByTenantId(tenantId);
        return products.stream().map(ProductMapper::toDto).toList();
    }

    public ProductDto getProductById(Long id, String tenantId) {
        Product product = repository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));
        return ProductMapper.toDto(product);
    }

    public ProductDto updateProduct(Long id, ProductDto dto) {
        if (dto == null) throw new IllegalArgumentException("Product cannot be null");
        Product product = repository.findByIdAndTenantId(id, dto.getTenantId())
                .orElseThrow(() -> new NoSuchElementException("Product not found with id " + id));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setTenantId(dto.getTenantId());

        return ProductMapper.toDto(repository.save(product));
    }

    public void deleteProduct(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Product not found with id " + id);
        }

        repository.deleteById(id);
    }
}
