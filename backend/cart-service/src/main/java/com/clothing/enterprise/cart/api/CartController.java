package com.clothing.enterprise.cart.api;

import com.clothing.enterprise.cart.dto.AddToCartRequest;
import com.clothing.enterprise.cart.dto.CartResponse;
import com.clothing.enterprise.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartResponse addToCart(
            @RequestHeader("X-User-Id") UUID userId,
            @Valid @RequestBody AddToCartRequest request) {
        return cartService.addToCart(userId, request);
    }

    @GetMapping
    public CartResponse getCart(@RequestHeader("X-User-Id") UUID userId) {
        return cartService.getCart(userId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@RequestHeader("X-User-Id") UUID userId) {
        cartService.clearCart(userId);
    }
}