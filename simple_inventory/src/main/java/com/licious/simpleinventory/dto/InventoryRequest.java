package com.licious.simpleinventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record InventoryRequest(
        @NotBlank(message = "productId must not be blank") String productId,
        @Positive(message = "quantity must be positive") int quantity
) {
}
