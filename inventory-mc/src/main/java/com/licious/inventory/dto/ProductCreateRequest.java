package com.licious.inventory.dto;

import com.licious.inventory.model.Category;
import com.licious.inventory.model.UnitOfMeasure;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProductCreateRequest(

        @NotBlank(message = "name is required")
        String name,

        @NotNull(message = "category is required")
        Category category,

        @NotNull(message = "unit is required")
        UnitOfMeasure unit,

        @NotNull(message = "pricePerUnit is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "pricePerUnit must be positive")
        BigDecimal pricePerUnit,

        @Min(value = 0, message = "initialQuantity cannot be negative")
        int initialQuantity,

        @Min(value = 0, message = "reorderThreshold cannot be negative")
        int reorderThreshold
) {
}
