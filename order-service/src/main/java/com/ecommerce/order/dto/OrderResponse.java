package com.ecommerce.order.dto;

import com.ecommerce.order.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        UUID productId,
        UUID customerId,
        String orderNotes,
        BigDecimal totalAmount,
        OrderStatus status,
        LocalDateTime createdAt
) {
}
