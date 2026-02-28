package com.clothing.enterprise.cart.repository;

import com.clothing.enterprise.cart.domain.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<CartEntity, UUID> {
    Optional<CartEntity> findByUserId(UUID userId);
}