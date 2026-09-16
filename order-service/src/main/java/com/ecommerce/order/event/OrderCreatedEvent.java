package com.ecommerce.order.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderCreatedEvent(
        UUID eventId,
        String eventType,
        UUID orderId,
        UUID productId,
        UUID customerId,
        BigDecimal totalAmount,
        LocalDateTime occurredAt
) {
}
