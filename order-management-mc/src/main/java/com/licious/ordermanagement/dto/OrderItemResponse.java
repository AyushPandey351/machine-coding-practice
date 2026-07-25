package com.licious.ordermanagement.dto;

import com.licious.ordermanagement.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productName,
        BigDecimal pricePerUnit,
        int quantity,
        BigDecimal subtotal
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getProductName(),
                item.getPricePerUnit(),
                item.getQuantity(),
                item.getSubtotal()
        );
    }
}
