package com.clothing.enterprise.cart.service;

import com.clothing.enterprise.cart.dto.AddToCartRequest;
import com.clothing.enterprise.cart.dto.CartResponse;
import java.util.UUID;

public interface CartService {
    CartResponse addToCart(UUID userId, AddToCartRequest request);
    CartResponse getCart(UUID userId);
    public void clearCart(UUID userId);
}