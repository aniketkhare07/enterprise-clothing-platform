package com.clothing.enterprise.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "product-service", url = "http://localhost:8082")
public interface ProductClient {

    // Ask Product Service for the specific Variant by SKU
    @GetMapping("/api/v1/products/variant/{sku}")
    ProductVariantDto getProductBySku(@PathVariable("sku") String sku);

    // DTO to hold the variant-specific data
    record ProductVariantDto(
            UUID productId,
            String productName,
            BigDecimal price,
            String sku,
            String size,
            String color
    ) {}
}