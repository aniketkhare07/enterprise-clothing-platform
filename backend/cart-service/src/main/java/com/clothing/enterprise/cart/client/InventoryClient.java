package com.clothing.enterprise.cart.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service", url = "http://localhost:8083")
public interface InventoryClient {

    @GetMapping("/api/v1/inventory/{sku}/check")
    boolean checkStock(@PathVariable("sku") String sku, @RequestParam("quantity") Integer quantity);
}