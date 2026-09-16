package com.ecommerce.order.exception;

public class ProductServiceUnavailableException extends RuntimeException {

    public ProductServiceUnavailableException() {
        super("Product Service is currently unavailable");
    }
}
