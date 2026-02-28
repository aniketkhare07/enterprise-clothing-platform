package com.clothing.enterprise.cart.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(
        UUID cartId,
        UUID userId,
        BigDecimal totalAmount,
        List<CartItemResponse> items
) {
    public record CartItemResponse(
            UUID productId,
            String sku,
            String productName,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal subTotal
    ) {}
}