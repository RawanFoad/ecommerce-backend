package com.cgm.ecommerce.controller;

import com.cgm.ecommerce.dto.OrderDto;
import com.cgm.ecommerce.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public OrderDto create(@RequestBody OrderDto dto, @RequestParam String tenantId) {
        return service.createOrder(dto, tenantId);
    }

    @GetMapping
    public List<OrderDto> getAll(@RequestParam String tenantId) {
        return service.getAllOrders(tenantId);
    }

    @GetMapping("/{id}")
    public OrderDto getById(@PathVariable Long id) {
        return service.getOrderById(id);
    }
}
