package com.insurance.claims_service.client;

import com.insurance.claims_service.dto.PolicyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "policy-service"
//        url = "${policy.service.url}"
        //dont need explicit url when running Eureka
)
public interface PolicyClient {
    @GetMapping("/api/v1/policies/{id}")
    PolicyResponse getPolicyById(@PathVariable UUID id);
}
