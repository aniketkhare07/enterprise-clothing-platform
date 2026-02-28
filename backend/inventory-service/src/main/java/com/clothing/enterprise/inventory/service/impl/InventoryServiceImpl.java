package com.clothing.enterprise.inventory.service.impl;

import com.clothing.enterprise.common.exception.BusinessException;
import com.clothing.enterprise.inventory.client.ProductClient;
import com.clothing.enterprise.inventory.domain.InventoryEntity;
import com.clothing.enterprise.inventory.dto.InventoryResponse;
import com.clothing.enterprise.inventory.dto.StockUpdateRequest;
import com.clothing.enterprise.inventory.repository.InventoryRepository;
import com.clothing.enterprise.inventory.service.InventoryService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductClient productClient;

    @Transactional(readOnly = true)
    public boolean isInStock(String sku, Integer quantityNeeded) {
        return inventoryRepository.findBySku(sku)
                .map(inventory -> (inventory.getQuantity() - inventory.getReservedQuantity()) >= quantityNeeded)
                .orElse(false); // If SKU not found, it's not in stock
    }

    @Transactional(readOnly = true)
    public InventoryResponse getStock(String sku) {
        return inventoryRepository.findBySku(sku)
                .map(inv -> new InventoryResponse(inv.getSku(), inv.getQuantity() - inv.getReservedQuantity()))
                .orElseThrow(() -> new RuntimeException("SKU not found"));
    }

    @Transactional
    public InventoryResponse updateStock(StockUpdateRequest request) {

        // 1. PRE-FLIGHT CHECK: Does this SKU exist in the catalog?
        try {
            productClient.getProductBySku(request.sku());
        } catch (FeignException.NotFound e) {
            // If Product Service throws a 404, we reject the inventory update!
            throw new BusinessException("Cannot add stock: SKU '" + request.sku() + "' does not exist in the product catalog.");
        }

        // 2. Proceed with normal save logic
        InventoryEntity inventory = inventoryRepository.findBySku(request.sku())
                .orElseGet(() -> InventoryEntity.builder()
                        .sku(request.sku())
                        .quantity(0)
                        .reservedQuantity(0)
                        .build());

        inventory.setQuantity(request.quantity());
        InventoryEntity saved = inventoryRepository.save(inventory);

        return new InventoryResponse(saved.getSku(), saved.getQuantity() - saved.getReservedQuantity());
    }

    @Transactional
    public void deductStock(String sku, Integer quantityToDeduct) {
        InventoryEntity inventory = inventoryRepository.findBySku(sku)
                .orElseThrow(() -> new BusinessException("SKU not found in inventory"));

        if (inventory.getQuantity() < quantityToDeduct) {
            throw new BusinessException("Insufficient stock for SKU: " + sku);
        }

        inventory.setQuantity(inventory.getQuantity() - quantityToDeduct);
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void restoreStock(String sku, Integer quantityToRestore) {
        InventoryEntity inventory = inventoryRepository.findBySku(sku)
                .orElseThrow(() -> new BusinessException("SKU not found in inventory"));

        inventory.setQuantity(inventory.getQuantity() + quantityToRestore);
        inventoryRepository.save(inventory);
    }
}