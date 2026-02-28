package com.clothing.enterprise.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", url = "http://localhost:8083")
public interface InventoryClient {

    record StockDeductRequest(String sku, Integer quantity) {}

    @PostMapping("/api/v1/inventory/deduct")
    void deductStock(@RequestBody StockDeductRequest request);

    @PostMapping("/api/v1/inventory/restore")
    void restoreStock(@RequestBody StockDeductRequest request);
}