package com.insurance.claims_service.repository;

import com.insurance.claims_service.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ClaimRepository extends JpaRepository<Claim, UUID> {

}
