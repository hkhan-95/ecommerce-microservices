package com.insurance.policy.controller;

import com.insurance.policy.dto.CreatePolicyRequest;
import com.insurance.policy.dto.PolicyResponse;
import com.insurance.policy.service.PolicyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {
    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<PolicyResponse> createPolicy(
            @Valid @RequestBody CreatePolicyRequest request){
        PolicyResponse response = policyService.createPolicy(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PolicyResponse>> getPolicies(){
        return ResponseEntity.ok(
                policyService.getAllPolicies()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicyById(
            @PathVariable UUID id
    ){
        return ResponseEntity.ok(
                policyService.getPolicyById(id)
        );
    }
}
