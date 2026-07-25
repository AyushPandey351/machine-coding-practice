package com.licious.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

/**
 * All fields optional - null means "leave unchanged" (partial update).
 */
public record ProductUpdateRequest(

        String name,

        @DecimalMin(value = "0.0", inclusive = false, message = "pricePerUnit must be positive")
        BigDecimal pricePerUnit,

        @Min(value = 0, message = "reorderThreshold cannot be negative")
        Integer reorderThreshold
) {
}
