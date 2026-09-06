package com.insurance.claims_service.dto;

import com.insurance.claims_service.entity.ClaimType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateClaimRequest(
        @NotNull
        UUID policyId,

        @NotNull
        UUID customerId,

        @NotNull
        ClaimType claimType,

        @NotBlank
        String description,

        @NotNull
        @Positive
        BigDecimal amountRequested
) {

}
