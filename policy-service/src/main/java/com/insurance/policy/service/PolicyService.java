package com.insurance.policy.service;

import com.insurance.policy.dto.CreatePolicyRequest;
import com.insurance.policy.dto.PolicyResponse;
import com.insurance.policy.entity.Policy;
import com.insurance.policy.entity.PolicyStatus;
import com.insurance.policy.exception.PolicyNotFoundException;
import com.insurance.policy.mapper.PolicyMapper;
import com.insurance.policy.repository.PolicyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PolicyService {
    private final PolicyRepository policyRepository;

    public PolicyService(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public PolicyResponse createPolicy(CreatePolicyRequest request){
        Policy policy = PolicyMapper.toEntity(request);

        policy.setStatus(PolicyStatus.ACTIVE);
        Policy savedPolicy = policyRepository.save(policy);

        return PolicyMapper.toResponse(savedPolicy);
    }

    public PolicyResponse getPolicyById(UUID id){
        Policy policy = policyRepository.findById(id)
                .orElseThrow(() -> new PolicyNotFoundException(id));

        return PolicyMapper.toResponse(policy);
    }

    public List<PolicyResponse> getAllPolicies(){
        return policyRepository.findAll()
                .stream()
                .map(policy -> PolicyMapper.toResponse(policy))
                .toList();
    }
}
