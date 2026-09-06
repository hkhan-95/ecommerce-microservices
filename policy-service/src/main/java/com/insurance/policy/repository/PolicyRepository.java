package com.insurance.policy.repository;

import com.insurance.policy.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PolicyRepository extends JpaRepository<Policy, UUID> {
    Optional<Policy> findByPolicyNumber(String policyNumber);
}
