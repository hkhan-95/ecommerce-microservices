package com.insurance.claims_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "claims")
@Getter
@Setter
@NoArgsConstructor
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "policy_id" ,nullable = false)
    private UUID policyId;

    @Column(name = "customer_id" ,nullable = false)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type" ,nullable = false)
    private ClaimType claimType;

    @Column(name = "description" ,nullable = false, length = 500)
    private String description;

    @Column(name = "amount_requested" ,nullable = false)
    private BigDecimal amountRequested;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
