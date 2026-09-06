package com.insurance.claims_service.controller;

import com.insurance.claims_service.client.PolicyClient;
import com.insurance.claims_service.dto.ClaimResponse;
import com.insurance.claims_service.dto.CreateClaimRequest;
import com.insurance.claims_service.dto.PolicyResponse;
import com.insurance.claims_service.service.ClaimService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(
            ClaimService claimService
    ) {
        this.claimService = claimService;
    }

    @PostMapping
    public ResponseEntity<ClaimResponse> createClaim(
            @Valid @RequestBody CreateClaimRequest request) {

        ClaimResponse response = claimService.createClaim(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> getClaimById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                claimService.getClaimById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<ClaimResponse>> getClaims() {

        return ResponseEntity.ok(
                claimService.getAllClaims()
        );
    }
}