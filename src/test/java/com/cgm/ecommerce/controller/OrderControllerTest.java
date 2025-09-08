package com.cgm.ecommerce.controller;

import com.cgm.ecommerce.dto.OrderDto;
import com.cgm.ecommerce.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService service;

    @Test
    void testCreateOrder() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setId(1L);
        dto.setCustomerId(10L);
        dto.setCreatedAt(LocalDateTime.now());

        Mockito.when(service.createOrder(any(OrderDto.class), eq("tenant1")))
                .thenReturn(dto);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("tenantId", "tenant1")
                        .content("""
                                {
                                  "customerId": 10,
                                  "items": []
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerId").value(10));
    }

    @Test
    void testGetAllOrders() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setId(1L);
        dto.setCustomerId(10L);
        dto.setTotalAmount(BigDecimal.valueOf(100));

        Mockito.when(service.getAllOrders("tenant1"))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/orders")
                        .param("tenantId", "tenant1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].customerId").value(10))
                .andExpect(jsonPath("$[0].totalAmount").value(100));
    }

    @Test
    void testGetOrderById() throws Exception {
        OrderDto dto = new OrderDto();
        dto.setId(1L);
        dto.setCustomerId(10L);

        Mockito.when(service.getOrderById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.customerId").value(10));
    }
}
