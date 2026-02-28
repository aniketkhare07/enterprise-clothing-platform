package com.clothing.enterprise.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequest(
        @NotBlank String name,
        String description,
        @NotNull @DecimalMin("0.01") BigDecimal basePrice,
        @NotBlank String categoryName,
        String imageUrl,
        @Valid List<VariantRequest> variants
) {
    public record VariantRequest(
            @NotBlank String sku,
            String size,
            String color,
            BigDecimal priceAdjustment
    ) {}
}