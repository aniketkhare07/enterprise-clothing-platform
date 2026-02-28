package com.clothing.enterprise.order.api;

import com.clothing.enterprise.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, UUID> checkout(@RequestHeader("X-User-Id") UUID userId) {
        UUID orderId = orderService.checkout(userId);
        return Map.of("orderId", orderId);
    }
}