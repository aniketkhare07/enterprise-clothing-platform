package com.clothing.enterprise.inventory.dto;

public record InventoryResponse(
        String sku,
        Integer availableQuantity
) {}