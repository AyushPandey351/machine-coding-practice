package com.licious.ordermanagement.entity;

import com.licious.ordermanagement.exception.InvalidQuantityException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private BigDecimal pricePerUnit;

    @Column(nullable = false)
    private int quantity;

    public OrderItem(Order order, String productName, BigDecimal pricePerUnit, int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException("quantity must be positive");
        }
        if (pricePerUnit == null || pricePerUnit.signum() <= 0) {
            throw new InvalidQuantityException("pricePerUnit must be positive");
        }
        this.order = order;
        this.productName = productName;
        this.pricePerUnit = pricePerUnit;
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return pricePerUnit.multiply(BigDecimal.valueOf(quantity));
    }
}
