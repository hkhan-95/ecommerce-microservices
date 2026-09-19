package com.ecommerce.order.controller;

import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.service.OrderService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(
            OrderService orderService
    ) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            JwtAuthenticationToken authentication) {

        String customerIdClaim = authentication.getToken().getClaimAsString("customerId");
        UUID customerId = UUID.fromString(customerIdClaim);

        OrderResponse response = orderService.createOrder(request, customerId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders(JwtAuthenticationToken authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin){
            return ResponseEntity.ok(
                    orderService.getAllOrders()
            );
        }

        String customerIdClaim = authentication.getToken().getClaimAsString("customerId");

        UUID customerId = UUID.fromString(customerIdClaim);

        return ResponseEntity.ok(
                orderService.getOrdersByCustomerId(customerId)
        );
    }
}
