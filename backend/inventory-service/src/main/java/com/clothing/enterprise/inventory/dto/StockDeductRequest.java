package com.clothing.enterprise.inventory.dto;

public record StockDeductRequest(
        String sku,
        Integer quantity
) {}