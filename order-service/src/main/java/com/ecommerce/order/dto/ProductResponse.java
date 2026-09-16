package com.ecommerce.order.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String sku,
        UUID sellerId,
        String name,
        String category,
        BigDecimal price,
        ProductStatus status,
        LocalDate availableFrom,
        LocalDate availableUntil,
        LocalDateTime createdAt
) {
}
