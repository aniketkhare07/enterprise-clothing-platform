package com.clothing.enterprise.order.service;

import java.util.UUID;

public interface OrderService {
    public UUID checkout(UUID userId);
}
