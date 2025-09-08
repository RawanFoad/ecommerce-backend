package com.cgm.ecommerce.mapper;

import com.cgm.ecommerce.domain.Order;
import com.cgm.ecommerce.dto.OrderDto;
import com.cgm.ecommerce.dto.OrderItemDto;

import java.util.List;

public class OrderMapper {
    public static OrderDto toDto(Order order) {
        if (order == null) return null;
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        if (order.getCustomer() != null) {
            dto.setCustomerId(order.getCustomer().getId());
        }
        dto.setCreatedAt(order.getCreatedAt());
        List<OrderItemDto> items = order.getItems().stream().map(item -> {
            OrderItemDto i = new OrderItemDto();
            i.setProductId(item.getProduct().getId());
            i.setQuantity(item.getQuantity());
            return i;
        }).toList();
        dto.setItems(items);
        return dto;
    }
}
