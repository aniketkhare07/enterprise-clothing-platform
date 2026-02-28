package com.clothing.enterprise.order.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;
    private BigDecimal totalAmount;
    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItemEntity> items = new ArrayList<>();

    public void addItem(OrderItemEntity item) {
        items.add(item);
        item.setOrder(this);
    }
}