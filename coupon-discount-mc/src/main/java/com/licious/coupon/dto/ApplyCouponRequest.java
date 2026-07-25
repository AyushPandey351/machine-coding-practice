package com.licious.coupon.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ApplyCouponRequest(
        @NotNull(message = "userId is required")
        Long userId,

        @NotEmpty(message = "couponCode is required")
        String couponCode,

        @NotEmpty(message = "cart must contain at least one item")
        @Valid
        List<CartItemRequest> items
) {
}
