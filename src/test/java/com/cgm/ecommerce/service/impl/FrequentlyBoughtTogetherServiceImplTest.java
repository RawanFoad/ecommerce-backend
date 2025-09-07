package com.cgm.ecommerce.service.impl;

import com.cgm.ecommerce.domain.Order;
import com.cgm.ecommerce.domain.OrderItem;
import com.cgm.ecommerce.domain.Product;
import com.cgm.ecommerce.dto.FrequentlyBoughtTogetherDto;
import com.cgm.ecommerce.dto.ProductDto;
import com.cgm.ecommerce.repository.OrderItemRepository;
import com.cgm.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FrequentlyBoughtTogetherServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FrequentlyBoughtTogetherServiceImpl service;

    @Test
    void testGetFrequentlyBoughtTogether_NoOrders() {
        Long productId = 1L;
        String tenantId = "tenant1";

        when(orderItemRepository.findOrdersContainingProduct(productId)).thenReturn(Collections.emptyList());

        FrequentlyBoughtTogetherDto result = service.getFrequentlyBoughtTogether(productId, 3, tenantId);

        assertEquals(productId, result.getBaseProductId());
        assertTrue(result.getSuggestedProducts().isEmpty());
    }

    @Test
    void testGetFrequentlyBoughtTogether_WithOrders() {
        Long productId = 1L;
        String tenantId = "tenant1";

        Product product2 = new Product();
        product2.setId(2L);
        Product product3 = new Product();
        product3.setId(3L);

        OrderItem item1 = new OrderItem();
        item1.setProduct(product2);

        OrderItem item2 = new OrderItem();
        item2.setProduct(product3);

        Order order = new Order();
        order.setTenantId(tenantId);
        order.setItems(Arrays.asList(item1, item2));

        when(orderItemRepository.findOrdersContainingProduct(productId)).thenReturn(List.of(order));
        when(productRepository.findAllById(List.of(2L, 3L))).thenReturn(List.of(product2, product3));

        FrequentlyBoughtTogetherDto result = service.getFrequentlyBoughtTogether(productId, 2, tenantId);

        assertEquals(productId, result.getBaseProductId());
        assertEquals(2, result.getSuggestedProducts().size());

        List<Long> suggestedIds = result.getSuggestedProducts().stream().map(ProductDto::getId).toList();
        assertTrue(suggestedIds.containsAll(List.of(2L, 3L)));
    }

    @Test
    void testGetFrequentlyBoughtTogether_TenantIsolation() {
        Long productId = 1L;
        String tenantId = "tenant1";

        Product product2 = new Product();
        product2.setId(2L);

        OrderItem item1 = new OrderItem();
        item1.setProduct(product2);

        Order order = new Order();
        order.setTenantId("otherTenant"); // different tenant
        order.setItems(List.of(item1));

        when(orderItemRepository.findOrdersContainingProduct(productId)).thenReturn(List.of(order));

        FrequentlyBoughtTogetherDto result = service.getFrequentlyBoughtTogether(productId, 1, tenantId);

        // Should not include products from other tenants
        assertTrue(result.getSuggestedProducts().isEmpty());
    }
}
