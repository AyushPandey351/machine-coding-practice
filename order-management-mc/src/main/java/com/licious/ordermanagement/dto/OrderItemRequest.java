package com.licious.ordermanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderItemRequest(
        @NotBlank(message = "productName is required")
        String productName,

        @NotNull(message = "pricePerUnit is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "pricePerUnit must be positive")
        BigDecimal pricePerUnit,

        @Positive(message = "quantity must be positive")
        int quantity
) {
}
