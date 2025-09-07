package com.cgm.ecommerce.service;

import com.cgm.ecommerce.dto.FrequentlyBoughtTogetherDto;

public interface FrequentlyBoughtTogetherService {
    FrequentlyBoughtTogetherDto getFrequentlyBoughtTogether(Long productId, int topN, String tenantId);
}

