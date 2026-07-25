package com.licious.inventory.dto;

import com.licious.inventory.entity.Product;
import com.licious.inventory.model.Category;
import com.licious.inventory.model.UnitOfMeasure;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        Category category,
        UnitOfMeasure unit,
        BigDecimal pricePerUnit,
        int quantityAvailable,
        int reorderThreshold,
        boolean lowStock
) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getUnit(),
                product.getPricePerUnit(),
                product.getQuantityAvailable(),
                product.getReorderThreshold(),
                product.isLowStock()
        );
    }
}
