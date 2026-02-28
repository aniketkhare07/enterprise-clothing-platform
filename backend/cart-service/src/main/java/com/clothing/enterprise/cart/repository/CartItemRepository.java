package com.clothing.enterprise.cart.repository;

import com.clothing.enterprise.cart.domain.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, UUID> {

    // @Modifying tells Spring this is a DELETE/UPDATE, not a SELECT
    @Modifying
    @Query("DELETE FROM CartItemEntity c WHERE c.cart.id = :cartId")
    void deleteAllByCartId(UUID cartId);
}