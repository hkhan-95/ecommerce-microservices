package com.insurance.policy.entity;

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
@Table(name = "policies")
@Getter
@Setter
@NoArgsConstructor
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "policy_number" ,nullable = false, unique = true)
    private String policyNumber;

    @Column(name = "customer_id" ,nullable = false)
    private UUID customerId;

    @Column(name = "vehicle_vin" ,nullable = false, length = 17)
    private String vehicleVin;

    @Enumerated(EnumType.STRING)
    @Column(name = "coverage_type" ,nullable = false)
    private CoverageType coverageType;

    @Column(name = "coverage_limit" ,nullable = false)
    private BigDecimal coverageLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PolicyStatus status;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
