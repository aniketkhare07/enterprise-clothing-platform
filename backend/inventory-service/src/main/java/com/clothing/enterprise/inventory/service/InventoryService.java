package com.clothing.enterprise.inventory.service;

import com.clothing.enterprise.inventory.dto.InventoryResponse;
import com.clothing.enterprise.inventory.dto.StockUpdateRequest;

public interface InventoryService {
    public boolean isInStock(String sku, Integer quantityNeeded);
    public InventoryResponse getStock(String sku);
    public InventoryResponse updateStock(StockUpdateRequest request);
    public void deductStock (String sku, Integer quantity);
    public void restoreStock(String sku, Integer quantityToRestore);
}
