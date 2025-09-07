package com.cgm.ecommerce.dto;

import java.util.List;

public class FrequentlyBoughtTogetherDto {
    private Long baseProductId;
    private List<ProductDto> suggestedProducts;

    public FrequentlyBoughtTogetherDto(Long baseProductId, List<ProductDto> suggestedProducts) {
        this.baseProductId = baseProductId;
        this.suggestedProducts = suggestedProducts;
    }

    public Long getBaseProductId() {
        return baseProductId;
    }

    public void setBaseProductId(Long baseProductId) {
        this.baseProductId = baseProductId;
    }

    public List<ProductDto> getSuggestedProducts() {
        return suggestedProducts;
    }

    public void setSuggestedProducts(List<ProductDto> suggestedProducts) {
        this.suggestedProducts = suggestedProducts;
    }
}
