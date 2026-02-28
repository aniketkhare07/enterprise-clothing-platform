package com.clothing.enterprise.order.repository;

import com.clothing.enterprise.order.domain.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}