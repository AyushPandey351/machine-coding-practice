package com.licious.ordermanagement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record OrderCreateRequest(
        @NotBlank(message = "customerName is required")
        String customerName,

        @NotEmpty(message = "order must contain at least one item")
        @Valid
        List<OrderItemRequest> items
) {
}
