package com.licious.simpleinventory.dto;

public record InventoryResponse(
        String productId,
        int quantity,
        String message
) {
}
