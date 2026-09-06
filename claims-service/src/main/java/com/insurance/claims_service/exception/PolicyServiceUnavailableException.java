package com.insurance.claims_service.exception;

public class PolicyServiceUnavailableException extends RuntimeException{

    public PolicyServiceUnavailableException(){
        super("Policy Service is currently unavailable");
    }
}
