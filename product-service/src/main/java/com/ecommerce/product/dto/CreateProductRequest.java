package com.ecommerce.product.dto;

import com.ecommerce.product.entity.ProductCategory;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateProductRequest(
        @NotBlank
        String sku,

        @NotNull
        UUID sellerId,

        @NotBlank
        String name,

        @NotNull
        ProductCategory category,

        @NotNull
        @Positive
        BigDecimal price,

        @NotNull
        LocalDate availableFrom,

        @NotNull
        LocalDate availableUntil
) {

}
