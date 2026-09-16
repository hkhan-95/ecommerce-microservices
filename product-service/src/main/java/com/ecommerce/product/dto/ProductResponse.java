package com.ecommerce.product.dto;

import com.ecommerce.product.entity.ProductCategory;
import com.ecommerce.product.entity.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String sku,
        UUID sellerId,
        String name,
        ProductCategory category,
        BigDecimal price,
        ProductStatus status,
        LocalDate availableFrom,
        LocalDate availableUntil,
        LocalDateTime createdAt
) {
}
