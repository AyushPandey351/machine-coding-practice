package com.licious.coupon.dto;

import com.licious.coupon.entity.Coupon;
import com.licious.coupon.entity.Product;
import com.licious.coupon.model.DiscountType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CouponResponse(
        Long id,
        String code,
        DiscountType discountType,
        BigDecimal discountValue,
        BigDecimal minCartValue,
        BigDecimal maxDiscountAmount,
        List<Long> applicableProductIds,
        int usageLimitPerUser,
        Integer totalUsageLimit,
        int currentUsageCount,
        Instant expiryDate,
        boolean active
) {
    public static CouponResponse from(Coupon coupon) {
        List<Long> productIds = coupon.getApplicableProducts().stream().map(Product::getId).toList();
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getDiscountType(),
                coupon.getDiscountValue(),
                coupon.getMinCartValue(),
                coupon.getMaxDiscountAmount(),
                productIds,
                coupon.getUsageLimitPerUser(),
                coupon.getTotalUsageLimit(),
                coupon.getCurrentUsageCount(),
                coupon.getExpiryDate(),
                coupon.isActive()
        );
    }
}
