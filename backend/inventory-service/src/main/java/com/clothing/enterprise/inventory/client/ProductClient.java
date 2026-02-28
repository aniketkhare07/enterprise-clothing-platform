package com.clothing.enterprise.inventory.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "http://localhost:8082")
public interface ProductClient {

    // We don't even need the data, we just need to know if it returns a 200 OK or a 404
    @GetMapping("/api/v1/products/variant/{sku}")
    Object getProductBySku(@PathVariable("sku") String sku);
}