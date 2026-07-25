package com.licious.coupon.dto;

import com.licious.coupon.model.DiscountType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CouponCreateRequest(
        @NotBlank(message = "code is required")
        String code,

        @NotNull(message = "discountType is required")
        DiscountType discountType,

        @NotNull(message = "discountValue is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "discountValue must be positive")
        BigDecimal discountValue,

        @NotNull(message = "minCartValue is required")
        @DecimalMin(value = "0.0", message = "minCartValue cannot be negative")
        BigDecimal minCartValue,

        @DecimalMin(value = "0.0", inclusive = false, message = "maxDiscountAmount must be positive if provided")
        BigDecimal maxDiscountAmount,

        /** Empty or omitted = applies to the whole cart. */
        List<Long> applicableProductIds,

        @Min(value = 1, message = "usageLimitPerUser must be at least 1")
        int usageLimitPerUser,

        /** null = unlimited total redemptions. */
        @Positive(message = "totalUsageLimit must be positive if provided")
        Integer totalUsageLimit,

        @NotNull(message = "expiryDate is required")
        @Future(message = "expiryDate must be in the future")
        Instant expiryDate
) {
}
