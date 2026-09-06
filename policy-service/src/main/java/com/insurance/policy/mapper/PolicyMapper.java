package com.insurance.policy.mapper;

import com.insurance.policy.dto.CreatePolicyRequest;
import com.insurance.policy.dto.PolicyResponse;
import com.insurance.policy.entity.Policy;

public class PolicyMapper {
    public static Policy toEntity(CreatePolicyRequest request) {
        Policy policy = new Policy();

        policy.setPolicyNumber(request.policyNumber());
        policy.setCustomerId(request.customerId());
        policy.setVehicleVin(request.vehicleVin());
        policy.setCoverageType(request.coverageType());
        policy.setCoverageLimit(request.coverageLimit());
        policy.setEffectiveDate(request.effectiveDate());
        policy.setExpirationDate(request.expirationDate());

        return policy;
    }

    public static PolicyResponse toResponse(Policy policy) {
        return new PolicyResponse(
                policy.getId(),
                policy.getPolicyNumber(),
                policy.getCustomerId(),
                policy.getVehicleVin(),
                policy.getCoverageType(),
                policy.getCoverageLimit(),
                policy.getStatus(),
                policy.getEffectiveDate(),
                policy.getExpirationDate(),
                policy.getCreatedAt()
        );
    }

}
