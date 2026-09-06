package com.insurance.claims_service.exception;

import java.util.UUID;

public class PolicyInactiveException extends RuntimeException {
    public PolicyInactiveException(UUID policyId) {
        super("Policy is not active: " + policyId);
    }
}
