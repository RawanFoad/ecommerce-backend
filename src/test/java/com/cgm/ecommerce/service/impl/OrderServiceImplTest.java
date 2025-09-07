package com.cgm.ecommerce.service.impl;

import com.cgm.ecommerce.domain.Customer;
import com.cgm.ecommerce.domain.Order;
import com.cgm.ecommerce.domain.Product;
import com.cgm.ecommerce.dto.OrderDto;
import com.cgm.ecommerce.dto.OrderItemDto;
import com.cgm.ecommerce.repository.CustomerRepository;
import com.cgm.ecommerce.repository.OrderRepository;
import com.cgm.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testCreateOrder_Success() {
        String tenantId = "tenant1";

        Customer customer = new Customer();
        customer.setId(1L);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal("10.0"));
        product.setStockQuantity(5);
        product.setTenantId(tenantId);

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProductId(1L);
        itemDto.setQuantity(2);

        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setItems(List.of(itemDto));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderDto created = orderService.createOrder(orderDto, tenantId);

        assertNotNull(created);
        assertEquals(1L, created.getCustomerId());
        assertEquals(1, created.getItems().size());
        assertEquals(3, product.getStockQuantity()); // 5 - 2
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrder_CustomerNotFound() {
        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(99L);

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.createOrder(orderDto, "tenant1"));

        assertEquals("Customer not found", ex.getMessage());
    }

    @Test
    void testCreateOrder_ProductNotFound() {
        Customer customer = new Customer();
        customer.setId(1L);

        OrderItemDto itemDto = new OrderItemDto();
        itemDto.setProductId(99L);
        itemDto.setQuantity(1);

        OrderDto orderDto = new OrderDto();
        orderDto.setCustomerId(1L);
        orderDto.setItems(List.of(itemDto));

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.createOrder(orderDto, "tenant1"));

        assertEquals("Product not found", ex.getMessage());
    }

    @Test
    void testGetAllOrders() {
        String tenantId = "tenant1";

        Order order = new Order();
        order.setId(1L);
        order.setTenantId(tenantId);
        order.setItems(new ArrayList<>());

        when(orderRepository.findByTenantId(tenantId)).thenReturn(List.of(order));

        List<OrderDto> orders = orderService.getAllOrders(tenantId);

        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getId());
    }

    @Test
    void testGetOrderById_Found() {
        Order order = new Order();
        order.setId(1L);
        order.setItems(new ArrayList<>());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        OrderDto dto = orderService.getOrderById(1L);

        assertEquals(1L, dto.getId());
    }

    @Test
    void testGetOrderById_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> orderService.getOrderById(1L));

        assertEquals("Order not found", ex.getMessage());
    }
}
