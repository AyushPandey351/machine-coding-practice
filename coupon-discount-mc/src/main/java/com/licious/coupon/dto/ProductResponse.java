package com.licious.coupon.dto;

import com.licious.coupon.entity.Product;
import com.licious.coupon.model.Category;

import java.math.BigDecimal;

public record ProductResponse(Long id, String name, Category category, BigDecimal price) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getCategory(), product.getPrice());
    }
}
