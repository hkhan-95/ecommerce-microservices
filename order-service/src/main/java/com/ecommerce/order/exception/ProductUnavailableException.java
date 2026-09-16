package com.ecommerce.order.exception;

import com.ecommerce.order.dto.ProductStatus;
import java.util.UUID;

public class ProductUnavailableException extends RuntimeException {
    public ProductUnavailableException(UUID productId, ProductStatus status) {
        super("Product is not available: " + productId + " (status: " + status + ")");
    }
}
