package com.cgm.ecommerce.service;

import com.cgm.ecommerce.dto.OrderDto;

import java.util.List;


public interface OrderService {
    OrderDto createOrder(OrderDto orderDto, String tenantId);
    List<OrderDto> getAllOrders(String tenantId);
    OrderDto getOrderById(Long id);
}
