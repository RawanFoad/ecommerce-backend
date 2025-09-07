package com.cgm.ecommerce.service.impl;

import com.cgm.ecommerce.domain.Order;
import com.cgm.ecommerce.domain.OrderItem;
import com.cgm.ecommerce.dto.FrequentlyBoughtTogetherDto;
import com.cgm.ecommerce.dto.ProductDto;
import com.cgm.ecommerce.mapper.ProductMapper;
import com.cgm.ecommerce.repository.OrderItemRepository;
import com.cgm.ecommerce.repository.ProductRepository;
import com.cgm.ecommerce.service.FrequentlyBoughtTogetherService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FrequentlyBoughtTogetherServiceImpl implements FrequentlyBoughtTogetherService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public FrequentlyBoughtTogetherServiceImpl(OrderItemRepository orderItemRepository,
                                               ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    public FrequentlyBoughtTogetherDto getFrequentlyBoughtTogether(Long productId, int topN, String tenantId) {
        // Step 1: Find orders containing the product
        List<Order> orders = orderItemRepository.findOrdersContainingProduct(productId);

        // Step 2: Collect co-purchased products
        Map<Long, Long> frequencyMap = new HashMap<>();
        for (Order order : orders) {
            if (!tenantId.equals(order.getTenantId())) continue; // enforce tenant isolation

            for (OrderItem item : order.getItems()) {
                Long otherProductId = item.getProduct().getId();
                if (!otherProductId.equals(productId)) {
                    frequencyMap.put(otherProductId, frequencyMap.getOrDefault(otherProductId, 0L) + 1);
                }
            }
        }

        // Step 3: Sort by frequency
        List<Long> topProductIds = frequencyMap.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(topN)
                .map(Map.Entry::getKey)
                .toList();

        // Step 4: Fetch product details
        List<ProductDto> suggestedProducts = productRepository.findAllById(topProductIds)
                .stream()
                .map(ProductMapper::toDto)
                .toList();

        return new FrequentlyBoughtTogetherDto(productId, suggestedProducts);
    }
}
