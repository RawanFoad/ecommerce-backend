package com.cgm.ecommerce.service.impl;

import com.cgm.ecommerce.domain.Customer;
import com.cgm.ecommerce.domain.Order;
import com.cgm.ecommerce.domain.OrderItem;
import com.cgm.ecommerce.domain.Product;
import com.cgm.ecommerce.dto.OrderDto;
import com.cgm.ecommerce.dto.OrderItemDto;
import com.cgm.ecommerce.mapper.OrderMapper;
import com.cgm.ecommerce.repository.CustomerRepository;
import com.cgm.ecommerce.repository.OrderRepository;
import com.cgm.ecommerce.repository.ProductRepository;
import com.cgm.ecommerce.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerRepository customerRepository,
                            ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public OrderDto createOrder(OrderDto orderDto, String tenantId) {
        Customer customer = customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setTenantId(tenantId);

        List<OrderItem> items = new ArrayList<>();

        // Validate stock and deduct
        for (OrderItemDto itemDto : orderDto.getItems()) {
            Product product = productRepository.findByIdAndTenantId(itemDto.getProductId(), tenantId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getPrice() == null) throw new RuntimeException("Product price missing");
            if (product.getTenantId() != null && !product.getTenantId().equals(tenantId))
                throw new RuntimeException("Product belongs to another tenant");

            if (itemDto.getQuantity() <= 0) throw new RuntimeException("Invalid quantity");
            // Assuming product has stock field
            int stock = Optional.ofNullable(product.getStockQuantity()).orElse(0);
            if (stock < itemDto.getQuantity())
                throw new RuntimeException("Insufficient stock for product: " + product.getName());

            product.setStockQuantity(stock - itemDto.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setOrder(order);
            items.add(orderItem);
        }

        order.setItems(items);
        orderRepository.save(order);
        return OrderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getAllOrders(String tenantId) {
        return orderRepository.findByTenantId(tenantId).stream().map(OrderMapper::toDto).toList();
    }

    @Override
    public OrderDto getOrderById(Long id) {
        return orderRepository.findById(id).map(OrderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }
}
