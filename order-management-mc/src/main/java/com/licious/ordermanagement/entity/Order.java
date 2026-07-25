package com.licious.ordermanagement.entity;

import com.licious.ordermanagement.exception.InvalidOrderStateException;
import com.licious.ordermanagement.model.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Status transitions are validated here so illegal moves (e.g. shipping a
 * cancelled order) are rejected in one place regardless of caller. Sequence
 * is CREATED -> CONFIRMED -> SHIPPED -> DELIVERED; CANCELLED is reachable
 * from CREATED or CONFIRMED only - once it has shipped, it's out for
 * delivery and can no longer be cancelled through this system.
 */
@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @Version
    private Long version;

    public Order(String customerName) {
        this.customerName = customerName;
        this.status = OrderStatus.CREATED;
        this.createdAt = Instant.now();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public BigDecimal getTotalAmount() {
        return items.stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void confirm() {
        requireStatus(OrderStatus.CREATED, "confirm");
        status = OrderStatus.CONFIRMED;
    }

    public void ship() {
        requireStatus(OrderStatus.CONFIRMED, "ship");
        status = OrderStatus.SHIPPED;
    }

    public void deliver() {
        requireStatus(OrderStatus.SHIPPED, "deliver");
        status = OrderStatus.DELIVERED;
    }

    public void cancel() {
        if (status != OrderStatus.CREATED && status != OrderStatus.CONFIRMED) {
            throw new InvalidOrderStateException(
                    "Cannot cancel order " + id + " in status " + status);
        }
        status = OrderStatus.CANCELLED;
    }

    private void requireStatus(OrderStatus required, String action) {
        if (status != required) {
            throw new InvalidOrderStateException(
                    "Cannot " + action + " order " + id + " in status " + status
                            + " (expected " + required + ")");
        }
    }
}
