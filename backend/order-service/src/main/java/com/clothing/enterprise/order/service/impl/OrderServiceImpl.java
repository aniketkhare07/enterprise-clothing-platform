package com.clothing.enterprise.order.service.impl;

import com.clothing.enterprise.common.exception.BusinessException;
import com.clothing.enterprise.order.client.CartClient;
import com.clothing.enterprise.order.client.InventoryClient;
import com.clothing.enterprise.order.domain.OrderEntity;
import com.clothing.enterprise.order.domain.OrderItemEntity;
import com.clothing.enterprise.order.dto.CartResponse;
import com.clothing.enterprise.order.repository.OrderRepository;
import com.clothing.enterprise.order.service.OrderService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartClient cartClient;
    private final InventoryClient inventoryClient;

    @Transactional
    public UUID checkout(UUID userId) {
        // 1. Fetch the user's cart
        CartResponse cart = cartClient.getCart();
        if (cart == null || cart.items().isEmpty()) {
            throw new BusinessException("Cannot proceed to checkout. Cart is empty.");
        }

        // 2. Create the Order in PENDING state
        OrderEntity order = OrderEntity.builder()
                .userId(userId)
                .totalAmount(cart.totalAmount())
                .status("PENDING")
                .build();

        for (CartResponse.CartItemResponse item : cart.items()) {
            order.addItem(OrderItemEntity.builder()
                    .sku(item.sku())
                    .productName(item.productName())
                    .quantity(item.quantity())
                    .unitPrice(item.unitPrice())
                    .subTotal(item.subTotal())
                    .build());
        }
        orderRepository.save(order);

        // 3. Attempt to deduct inventory for each item
        try {
            for (CartResponse.CartItemResponse item : cart.items()) {
                inventoryClient.deductStock(new InventoryClient.StockDeductRequest(item.sku(), item.quantity()));
            }
        } catch (FeignException e) {
            // SAGA ROLLBACK: If inventory fails, mark order as failed and abort!
            order.setStatus("FAILED");
            orderRepository.save(order);
            throw new BusinessException("Checkout failed: Insufficient stock for one or more items in your cart.");
        }

        try {
            order.setStatus("COMPLETED");
            orderRepository.saveAndFlush(order);
        } catch (Exception e) {
            for (CartResponse.CartItemResponse item : cart.items()) {
                inventoryClient.restoreStock(new InventoryClient.StockDeductRequest(item.sku(), item.quantity()));
            }
            throw new BusinessException("Critical error saving order. Inventory has been safely restored.");
        }

        cartClient.clearCart();

        return order.getId();
    }
}