package com.insurance.policy.dto;

import com.insurance.policy.entity.CoverageType;
import com.insurance.policy.entity.PolicyStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record PolicyResponse(
        UUID id,
        String policyNumber,
        UUID customerId,
        String vehicleVin,
        CoverageType coverageType,
        BigDecimal coverageLimit,
        PolicyStatus status,
        LocalDate effectiveDate,
        LocalDate expirationDate,
        LocalDateTime createdAt
) {
}
