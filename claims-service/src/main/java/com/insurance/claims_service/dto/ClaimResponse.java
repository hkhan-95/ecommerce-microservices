package com.insurance.claims_service.dto;

import com.insurance.claims_service.entity.ClaimStatus;
import com.insurance.claims_service.entity.ClaimType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClaimResponse(
        UUID id,
        UUID policyId,
        UUID customerId,
        ClaimType claimType,
        String description,
        BigDecimal amountRequested,
        ClaimStatus status,
        LocalDateTime createdAt
) {
}
