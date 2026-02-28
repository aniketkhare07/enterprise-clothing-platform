package com.clothing.enterprise.cart.service.impl;

import com.clothing.enterprise.cart.client.InventoryClient;
import com.clothing.enterprise.cart.client.ProductClient;
import com.clothing.enterprise.cart.domain.CartEntity;
import com.clothing.enterprise.cart.domain.CartItemEntity;
import com.clothing.enterprise.cart.dto.AddToCartRequest;
import com.clothing.enterprise.cart.dto.CartResponse;
import com.clothing.enterprise.cart.repository.CartItemRepository;
import com.clothing.enterprise.cart.repository.CartRepository;
import com.clothing.enterprise.cart.service.CartService;
import com.clothing.enterprise.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartResponse addToCart(UUID userId, AddToCartRequest request) {

        // 1. Fetch Variant Details (This gets the specific price and parent ID)
        ProductClient.ProductVariantDto variant = productClient.getProductBySku(request.sku());
        if (variant == null) {
            throw new BusinessException("Product variant not found for SKU: " + request.sku());
        }

        // 2. Check Inventory (Remains the same, checking specific SKU)
        boolean inStock = inventoryClient.checkStock(request.sku(), request.quantity());
        if (!inStock) {
            throw new BusinessException("Insufficient stock for SKU: " + request.sku());
        }

        // 3. Find or Create Cart
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CartEntity newCart = CartEntity.builder().userId(userId).build();
                    return cartRepository.save(newCart);
                });

        // 4. Update existing item OR Add new item
        Optional<CartItemEntity> existingItem = cart.getItems().stream()
                .filter(item -> item.getSku().equals(request.sku()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItemEntity item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.quantity());
            item.setSubTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } else {
            // Format a nice display name: "Developer Hoodie (Black/XL)"
            String displayName = String.format("%s (%s/%s)",
                    variant.productName(),
                    variant.color() != null ? variant.color() : "N/A",
                    variant.size() != null ? variant.size() : "N/A");

            CartItemEntity newItem = CartItemEntity.builder()
                    .productId(variant.productId()) // Retrieved from the Feign call
                    .sku(request.sku())
                    .productName(displayName)
                    .quantity(request.quantity())
                    .unitPrice(variant.price())     // The exact variant price
                    .subTotal(variant.price().multiply(BigDecimal.valueOf(request.quantity())))
                    .build();
            cart.addItem(newItem);
        }

        CartEntity savedCart = cartRepository.save(cart);
        return mapToResponse(savedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart(UUID userId) {
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new BusinessException("Cart empty"));
        return mapToResponse(cart);
    }

    private CartResponse mapToResponse(CartEntity cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItemEntity::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<CartResponse.CartItemResponse> items = cart.getItems().stream()
                .map(i -> new CartResponse.CartItemResponse(
                        i.getProductId(), i.getSku(), i.getProductName(),
                        i.getQuantity(), i.getUnitPrice(), i.getSubTotal()
                ))
                .toList();

        return new CartResponse(cart.getId(), cart.getUserId(), total, items);
    }

    @Transactional
    public void clearCart(UUID userId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cartItemRepository.deleteAllByCartId(cart.getId());
        });
    }
}