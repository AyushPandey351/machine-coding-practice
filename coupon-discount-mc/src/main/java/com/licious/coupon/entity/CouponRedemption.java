package com.licious.coupon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "coupon_redemptions")
@Getter
@NoArgsConstructor
public class CouponRedemption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal cartTotal;

    @Column(nullable = false)
    private BigDecimal discountAmount;

    @Column(nullable = false)
    private BigDecimal finalTotal;

    @Column(nullable = false)
    private Instant redeemedAt;

    public CouponRedemption(Coupon coupon, User user, BigDecimal cartTotal, BigDecimal discountAmount, BigDecimal finalTotal) {
        this.coupon = coupon;
        this.user = user;
        this.cartTotal = cartTotal;
        this.discountAmount = discountAmount;
        this.finalTotal = finalTotal;
        this.redeemedAt = Instant.now();
    }
}
