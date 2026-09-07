package com.insurance.claims_service.service;

import com.insurance.claims_service.client.PolicyClient;
import com.insurance.claims_service.dto.ClaimResponse;
import com.insurance.claims_service.dto.CreateClaimRequest;
import com.insurance.claims_service.dto.PolicyResponse;
import com.insurance.claims_service.entity.Claim;
import com.insurance.claims_service.entity.ClaimStatus;
import com.insurance.claims_service.event.ClaimCreatedEvent;
import com.insurance.claims_service.exception.PolicyInactiveException;
import com.insurance.claims_service.exception.PolicyNotFoundException;
import com.insurance.claims_service.exception.PolicyServiceUnavailableException;
import com.insurance.claims_service.mapper.ClaimMapper;
import com.insurance.claims_service.producer.ClaimEventProducer;
import com.insurance.claims_service.repository.ClaimRepository;
import feign.FeignException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PolicyClient policyClient;
    private final ClaimEventProducer claimEventProducer;

    public ClaimService(
            ClaimRepository claimRepository,
            PolicyClient policyClient,
            ClaimEventProducer claimEventProducer
    ) {
        this.claimRepository = claimRepository;
        this.policyClient = policyClient;
        this.claimEventProducer = claimEventProducer;
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

        ClaimCreatedEvent event = new ClaimCreatedEvent(
                UUID.randomUUID(),
                "CLAIM_CREATED",
                savedClaim.getId(),
                savedClaim.getPolicyId(),
                savedClaim.getCustomerId(),
                savedClaim.getClaimType().name(),
                savedClaim.getAmountRequested(),
                LocalDateTime.now()
        );

        claimEventProducer.publishClaimCreated(
                savedClaim.getId().toString(),
                event
        );

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