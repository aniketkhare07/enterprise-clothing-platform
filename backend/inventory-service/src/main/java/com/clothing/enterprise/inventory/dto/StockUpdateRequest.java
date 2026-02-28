package com.clothing.enterprise.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StockUpdateRequest(
        @NotBlank String sku,
        @NotNull @Min(0) Integer quantity
) {}