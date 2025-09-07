package com.cgm.ecommerce.controller;

import com.cgm.ecommerce.dto.FrequentlyBoughtTogetherDto;
import com.cgm.ecommerce.service.FrequentlyBoughtTogetherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/frequently-bought-together")
public class FrequentlyBoughtTogetherController {
    private final FrequentlyBoughtTogetherService service;

    public FrequentlyBoughtTogetherController(FrequentlyBoughtTogetherService service) {
        this.service = service;
    }

    @GetMapping("/{productId}")
    public FrequentlyBoughtTogetherDto getFrequentlyBoughtTogether(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "3") int topN,
            @RequestParam String tenantId) {
        return service.getFrequentlyBoughtTogether(productId, topN, tenantId);
    }
}
