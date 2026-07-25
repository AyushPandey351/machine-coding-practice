package com.licious.inventory.dto;

import jakarta.validation.constraints.Positive;

public record StockUpdateRequest(
        @Positive(message = "quantity must be positive")
        int quantity
) {
}
