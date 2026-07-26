package com.licious.simpleinventory.dto;

public record InventoryRequest(
        String productId,
        int quantity
) {
}
