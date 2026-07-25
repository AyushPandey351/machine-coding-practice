package com.licious.coupon.entity;

import com.licious.coupon.model.DiscountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * An empty applicableProducts set means "applies to the whole cart" - it's
 * deliberately not null so callers never need a null check.
 */
@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType;

    @Column(nullable = false)
    private BigDecimal discountValue;

    @Column(nullable = false)
    private BigDecimal minCartValue;

    private BigDecimal maxDiscountAmount;

    @ManyToMany
    @JoinTable(
            name = "coupon_applicable_products",
            joinColumns = @JoinColumn(name = "coupon_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> applicableProducts = new HashSet<>();

    @Column(nullable = false)
    private int usageLimitPerUser;

    /** null = unlimited total redemptions. */
    private Integer totalUsageLimit;

    @Column(nullable = false)
    private int currentUsageCount;

    @Column(nullable = false)
    private Instant expiryDate;

    @Column(nullable = false)
    private boolean active;

    @Version
    private Long version;

    public Coupon(String code, DiscountType discountType, BigDecimal discountValue, BigDecimal minCartValue,
                  BigDecimal maxDiscountAmount, Set<Product> applicableProducts, int usageLimitPerUser,
                  Integer totalUsageLimit, Instant expiryDate) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minCartValue = minCartValue;
        this.maxDiscountAmount = maxDiscountAmount;
        this.applicableProducts = applicableProducts != null ? applicableProducts : new HashSet<>();
        this.usageLimitPerUser = usageLimitPerUser;
        this.totalUsageLimit = totalUsageLimit;
        this.currentUsageCount = 0;
        this.expiryDate = expiryDate;
        this.active = true;
    }

    public boolean isExpired(Instant now) {
        return now.isAfter(expiryDate);
    }

    public boolean hasReachedGlobalLimit() {
        return totalUsageLimit != null && currentUsageCount >= totalUsageLimit;
    }

    public boolean appliesTo(Long productId) {
        return applicableProducts.isEmpty()
                || applicableProducts.stream().anyMatch(p -> p.getId().equals(productId));
    }

    /** Discount never exceeds the eligible cart amount, and is capped by maxDiscountAmount if set. */
    public BigDecimal computeDiscount(BigDecimal eligibleAmount) {
        BigDecimal raw = switch (discountType) {
            case FLAT -> discountValue;
            case PERCENTAGE -> eligibleAmount
                    .multiply(discountValue)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        };
        BigDecimal capped = maxDiscountAmount != null ? raw.min(maxDiscountAmount) : raw;
        return capped.min(eligibleAmount);
    }

    public void incrementUsage() {
        currentUsageCount++;
    }

    public void deactivate() {
        active = false;
    }
}
