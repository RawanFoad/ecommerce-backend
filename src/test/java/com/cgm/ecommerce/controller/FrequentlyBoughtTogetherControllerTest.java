package com.cgm.ecommerce.controller;

import com.cgm.ecommerce.dto.FrequentlyBoughtTogetherDto;
import com.cgm.ecommerce.dto.ProductDto;
import com.cgm.ecommerce.service.FrequentlyBoughtTogetherService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FrequentlyBoughtTogetherController.class)
class FrequentlyBoughtTogetherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FrequentlyBoughtTogetherService service;

    @Test
    void testGetFrequentlyBoughtTogether() throws Exception {
        // Arrange: create mock response
        ProductDto p1 = new ProductDto(2L, "Phone Case", "Protective case", new BigDecimal("19.99"));
        ProductDto p2 = new ProductDto(3L, "Screen Protector", "Tempered glass", new BigDecimal("9.99"));
        FrequentlyBoughtTogetherDto mockDto =
                new FrequentlyBoughtTogetherDto(1L, List.of(p1, p2));

        Mockito.when(service.getFrequentlyBoughtTogether(anyLong(), anyInt(), anyString()))
                .thenReturn(mockDto);

        // Act & Assert: perform GET request
        mockMvc.perform(get("/api/frequently-bought-together/1")
                        .param("topN", "2")
                        .param("tenantId", "tenant1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseProductId").value(1L))
                .andExpect(jsonPath("$.suggestedProducts[0].id").value(2L))
                .andExpect(jsonPath("$.suggestedProducts[0].name").value("Phone Case"))
                .andExpect(jsonPath("$.suggestedProducts[1].id").value(3L))
                .andExpect(jsonPath("$.suggestedProducts[1].name").value("Screen Protector"));
    }
}
