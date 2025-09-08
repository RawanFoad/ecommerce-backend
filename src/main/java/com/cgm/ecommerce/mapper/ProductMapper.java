package com.cgm.ecommerce.mapper;

import com.cgm.ecommerce.domain.Product;
import com.cgm.ecommerce.dto.ProductDto;

public class ProductMapper {
    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getTenantId()
        );
    }

    public static Product toEntity(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setTenantId(dto.getTenantId());
        return product;
    }
}
