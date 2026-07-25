package com.licious.coupon.dto;

import java.math.BigDecimal;

public record ApplyCouponResponse(
        String couponCode,
        BigDecimal cartTotal,
        BigDecimal eligibleAmount,
        BigDecimal discountAmount,
        BigDecimal finalTotal
) {
}
