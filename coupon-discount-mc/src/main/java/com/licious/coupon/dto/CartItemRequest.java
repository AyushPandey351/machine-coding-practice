package com.licious.coupon.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequest(
        @NotNull(message = "productId is required")
        Long productId,

        @Positive(message = "quantity must be positive")
        int quantity
) {
}
