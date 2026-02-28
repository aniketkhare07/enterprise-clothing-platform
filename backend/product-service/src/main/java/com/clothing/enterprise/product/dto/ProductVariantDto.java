package com.clothing.enterprise.product.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductVariantDto(
        UUID productId,
        String productName,
        BigDecimal price,
        String sku,
        String size,
        String color
) {}