package com.insurance.claims_service.mapper;

import com.insurance.claims_service.dto.ClaimResponse;
import com.insurance.claims_service.dto.CreateClaimRequest;
import com.insurance.claims_service.entity.Claim;

public class ClaimMapper {


    public static Claim toEntity(CreateClaimRequest request) {
        Claim claim = new Claim();

        claim.setPolicyId(request.policyId());
        claim.setCustomerId(request.customerId());
        claim.setClaimType(request.claimType());
        claim.setDescription(request.description());
        claim.setAmountRequested(request.amountRequested());

        return claim;
    }

    public static ClaimResponse toResponse(Claim claim) {
        return new ClaimResponse(
                claim.getId(),
                claim.getPolicyId(),
                claim.getCustomerId(),
                claim.getClaimType(),
                claim.getDescription(),
                claim.getAmountRequested(),
                claim.getStatus(),
                claim.getCreatedAt()
        );
    }

}
