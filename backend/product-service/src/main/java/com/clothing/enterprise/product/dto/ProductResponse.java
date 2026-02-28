package com.clothing.enterprise.product.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        BigDecimal basePrice,
        String categoryName,
        String imageUrl,
        List<VariantResponse> variants
) {
    public record VariantResponse(
            UUID id,
            String sku,
            String size,
            String color,
            BigDecimal priceAdjustment
    ) {}
}