package com.insurance.policy.dto;

import com.insurance.policy.entity.CoverageType;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreatePolicyRequest(
        @NotBlank
        String policyNumber,

        @NotNull
        UUID customerId,

        @NotBlank
        @Size(min = 17, max = 17)
        String vehicleVin,

        @NotNull
        CoverageType coverageType,

        @NotNull
        @Positive
        BigDecimal coverageLimit,

        @NotNull
        LocalDate effectiveDate,

        @NotNull
        LocalDate expirationDate
) {

}
