package com.clothing.enterprise.product.repository;

import com.clothing.enterprise.product.domain.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}