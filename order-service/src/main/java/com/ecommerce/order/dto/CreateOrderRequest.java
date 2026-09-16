package com.ecommerce.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateOrderRequest(
        @NotNull
        UUID productId,

        @NotNull
        UUID customerId,

        @NotBlank
        String orderNotes
) {
}
