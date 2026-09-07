package com.insurance.claims_service.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClaimCreatedEvent(
        UUID eventId,
        String eventType,
        UUID claimId,
        UUID policyId,
        UUID customerId,
        String claimType,
        BigDecimal amountRequested,
        LocalDateTime occurredAt
) {
}
