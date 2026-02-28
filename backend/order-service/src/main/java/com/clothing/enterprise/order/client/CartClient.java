package com.clothing.enterprise.order.client;

import com.clothing.enterprise.order.config.FeignClientInterceptor;
import com.clothing.enterprise.order.dto.CartResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cart-service", url = "http://localhost:8084", configuration = FeignClientInterceptor.class)
public interface CartClient {

    @GetMapping("/api/v1/cart")
    CartResponse getCart();

    @DeleteMapping("/api/v1/cart")
    void clearCart();
}