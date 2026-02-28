package com.clothing.enterprise.product.repository;

import com.clothing.enterprise.product.domain.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, UUID> {
    boolean existsBySku(String sku);
    Optional<ProductVariantEntity> findBySku(String sku);
}