package com.insurance.claims_service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record PolicyResponse(
        UUID id,
        String policyNumber,
        UUID customerId,
        String vehicleVin,
        String coverageType,
        BigDecimal coverageLimit,
        String status,
        LocalDate effectiveDate,
        LocalDate expirationDate
) {
}
