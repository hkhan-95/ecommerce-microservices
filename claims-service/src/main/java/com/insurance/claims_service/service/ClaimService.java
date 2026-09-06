package com.insurance.claims_service.service;

import com.insurance.claims_service.client.PolicyClient;
import com.insurance.claims_service.dto.ClaimResponse;
import com.insurance.claims_service.dto.CreateClaimRequest;
import com.insurance.claims_service.dto.PolicyResponse;
import com.insurance.claims_service.entity.Claim;
import com.insurance.claims_service.entity.ClaimStatus;
import com.insurance.claims_service.exception.PolicyInactiveException;
import com.insurance.claims_service.exception.PolicyNotFoundException;
import com.insurance.claims_service.exception.PolicyServiceUnavailableException;
import com.insurance.claims_service.mapper.ClaimMapper;
import com.insurance.claims_service.repository.ClaimRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyClient policyClient;

    public ClaimService(
            ClaimRepository claimRepository,
            PolicyClient policyClient
    ) {
        this.claimRepository = claimRepository;
        this.policyClient = policyClient;
    }

    public ClaimResponse createClaim(CreateClaimRequest request) {

        PolicyResponse policy;

        try {
            policy = policyClient.getPolicyById(request.policyId());
        } catch (FeignException.NotFound ex){
            throw new PolicyNotFoundException(request.policyId());
        } catch (feign.RetryableException ex) {
            throw new PolicyServiceUnavailableException();
        }

        if (!"ACTIVE".equals(policy.status())){
            throw new PolicyInactiveException(request.policyId());
        }

        Claim claim = ClaimMapper.toEntity(request);
        claim.setStatus(ClaimStatus.SUBMITTED);
        Claim savedClaim = claimRepository.save(claim);
        return ClaimMapper.toResponse(savedClaim);
    }

    public ClaimResponse getClaimById(UUID id) {

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Claim not found with id: " + id)
                );

        return ClaimMapper.toResponse(claim);
    }

    public List<ClaimResponse> getAllClaims() {
        return claimRepository.findAll()
                .stream()
                .map(claim -> ClaimMapper.toResponse(claim))
                .toList();
    }
}