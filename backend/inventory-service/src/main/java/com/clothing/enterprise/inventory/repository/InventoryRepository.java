package com.clothing.enterprise.inventory.repository;

import com.clothing.enterprise.inventory.domain.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository extends JpaRepository<InventoryEntity, UUID> {
    Optional<InventoryEntity> findBySku(String sku);
}