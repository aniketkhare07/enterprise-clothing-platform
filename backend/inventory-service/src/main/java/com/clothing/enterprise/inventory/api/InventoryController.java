package com.clothing.enterprise.inventory.api;

import com.clothing.enterprise.inventory.dto.InventoryResponse;
import com.clothing.enterprise.inventory.dto.StockDeductRequest;
import com.clothing.enterprise.inventory.dto.StockUpdateRequest;
import com.clothing.enterprise.inventory.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Get stock count: GET /api/v1/inventory/HOODIE-BLK-S
    @GetMapping("/{sku}")
    public InventoryResponse getStock(@PathVariable String sku) {
        return inventoryService.getStock(sku);
    }

    // Check availability (useful for Cart validation): GET /api/v1/inventory/HOODIE-BLK-S/check?quantity=5
    @GetMapping("/{sku}/check")
    public boolean isInStock(@PathVariable String sku, @RequestParam Integer quantity) {
        return inventoryService.isInStock(sku, quantity);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryResponse updateStock(@Valid @RequestBody StockUpdateRequest request) {
        return inventoryService.updateStock(request);
    }

    @PostMapping("/deduct")
    @ResponseStatus(HttpStatus.OK)
    public void deductStock(@RequestBody StockDeductRequest request) {
        inventoryService.deductStock(request.sku(), request.quantity());
    }

    @PostMapping("/restore")
    @ResponseStatus(HttpStatus.OK)
    public void restoreStock(@RequestBody StockDeductRequest request) {
        inventoryService.restoreStock(request.sku(), request.quantity());
    }
}