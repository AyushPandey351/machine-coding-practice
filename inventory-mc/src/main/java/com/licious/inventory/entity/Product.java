package com.licious.inventory.entity;

import com.licious.inventory.exception.InsufficientStockException;
import com.licious.inventory.exception.InvalidQuantityException;
import com.licious.inventory.model.Category;
import com.licious.inventory.model.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Stock mutations are exposed only through addStock/deductStock so the
 * "never negative" invariant is enforced in one place. Thread-safety for
 * concurrent stock updates is NOT handled here (a synchronized method would
 * be useless - Hibernate loads a fresh instance per transaction) - it's
 * handled at the repository/service layer via a pessimistic row lock.
 */
@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnitOfMeasure unit;

    @Column(nullable = false)
    private BigDecimal pricePerUnit;

    @Column(nullable = false)
    private int quantityAvailable;

    @Column(nullable = false)
    private int reorderThreshold;

    // Defense-in-depth: even without the pessimistic lock, a lost update
    // between load and commit would throw ObjectOptimisticLockingFailureException.
    @Version
    private Long version;

    public Product(String name, Category category, UnitOfMeasure unit, BigDecimal pricePerUnit,
                    int initialQuantity, int reorderThreshold) {
        if (initialQuantity < 0) {
            throw new InvalidQuantityException("Initial quantity cannot be negative");
        }
        this.name = name;
        this.category = category;
        this.unit = unit;
        this.pricePerUnit = pricePerUnit;
        this.quantityAvailable = initialQuantity;
        this.reorderThreshold = reorderThreshold;
    }

    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity to add must be positive");
        }
        quantityAvailable += quantity;
    }

    public void deductStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException("Quantity to deduct must be positive");
        }
        if (quantityAvailable < quantity) {
            throw new InsufficientStockException(
                    "Insufficient stock for product " + id + ": requested " + quantity
                            + ", available " + quantityAvailable);
        }
        quantityAvailable -= quantity;
    }

    public void updateDetails(String name, BigDecimal pricePerUnit, Integer reorderThreshold) {
        if (name != null) this.name = name;
        if (pricePerUnit != null) this.pricePerUnit = pricePerUnit;
        if (reorderThreshold != null) this.reorderThreshold = reorderThreshold;
    }

    public boolean isLowStock() {
        return quantityAvailable <= reorderThreshold;
    }
}
