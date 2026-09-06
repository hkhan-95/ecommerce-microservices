package com.insurance.policy.exception;

import java.util.UUID;

public class PolicyNotFoundException extends RuntimeException {
    public PolicyNotFoundException(UUID id){
        super("Policy not found with id: " + id);
    }
}
