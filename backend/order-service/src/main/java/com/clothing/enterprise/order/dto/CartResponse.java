package com.clothing.enterprise.order.dto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CartResponse(UUID cartId, UUID userId, BigDecimal totalAmount, List<CartItemResponse> items) {
    public record CartItemResponse(String sku, String productName, Integer quantity, BigDecimal unitPrice, BigDecimal subTotal) {}
}