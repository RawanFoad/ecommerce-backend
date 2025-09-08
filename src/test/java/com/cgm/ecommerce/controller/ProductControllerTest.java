package com.cgm.ecommerce.controller;

import com.cgm.ecommerce.dto.ProductDto;
import com.cgm.ecommerce.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Test
    void testCreateProduct() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setId(1L);
        dto.setName("Phone");
        dto.setDescription("Smartphone");
        dto.setPrice(BigDecimal.valueOf(599.99));

        Mockito.when(service.createProduct(any(ProductDto.class)))
                .thenReturn(dto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Phone",
                                  "description": "Smartphone",
                                  "price": 599.99
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.description").value("Smartphone"))
                .andExpect(jsonPath("$.price").value(599.99));
    }

    @Test
    void testGetAllProducts() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setId(1L);
        dto.setName("Laptop");
        dto.setDescription("Gaming Laptop");
        dto.setPrice(BigDecimal.valueOf(1200));

        Mockito.when(service.getAllProducts("tenant1"))
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/products")
                        .param("tenantId", "tenant1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].description").value("Gaming Laptop"))
                .andExpect(jsonPath("$[0].price").value(1200));
    }

    @Test
    void testGetProductById() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setId(2L);
        dto.setName("Tablet");
        dto.setDescription("Android Tablet");
        dto.setPrice(BigDecimal.valueOf(300));

        Mockito.when(service.getProductById(2L, "tenant1"))
                .thenReturn(dto);

        mockMvc.perform(get("/api/products/2")
                        .param("tenantId", "tenant1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Tablet"))
                .andExpect(jsonPath("$.description").value("Android Tablet"))
                .andExpect(jsonPath("$.price").value(300));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductDto dto = new ProductDto();
        dto.setId(3L);
        dto.setName("Updated Laptop");
        dto.setDescription("High-end Gaming Laptop");
        dto.setPrice(BigDecimal.valueOf(1500));

        Mockito.when(service.updateProduct(eq(3L), any(ProductDto.class)))
                .thenReturn(dto);

        mockMvc.perform(put("/api/products/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Updated Laptop",
                                  "description": "High-end Gaming Laptop",
                                  "price": 1500
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.description").value("High-end Gaming Laptop"))
                .andExpect(jsonPath("$.price").value(1500));
    }

    @Test
    void testDeleteProduct() throws Exception {
        Mockito.doNothing().when(service).deleteProduct(5L, "tenant1");

        mockMvc.perform(delete("/api/products/5")
                        .param("tenantId", "tenant1"))
                .andExpect(status().isOk());
    }
}
