package com.clothing.enterprise.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record AddToCartRequest(
        @NotNull UUID productId,
        @NotNull String sku,
        @NotNull @Min(1) Integer quantity
) {}