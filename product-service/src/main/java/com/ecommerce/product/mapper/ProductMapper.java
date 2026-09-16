package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.CreateProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.entity.Product;

public class ProductMapper {
    private ProductMapper() {
    }

    public static Product toEntity(CreateProductRequest request) {
        Product product = new Product();

        product.setSku(request.sku());
        product.setSellerId(request.sellerId());
        product.setName(request.name());
        product.setCategory(request.category());
        product.setPrice(request.price());
        product.setAvailableFrom(request.availableFrom());
        product.setAvailableUntil(request.availableUntil());

        return product;
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getSellerId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getStatus(),
                product.getAvailableFrom(),
                product.getAvailableUntil(),
                product.getCreatedAt()
        );
    }
}
