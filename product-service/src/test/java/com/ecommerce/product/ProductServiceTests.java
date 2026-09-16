package com.ecommerce.product;

import com.ecommerce.product.dto.CreateProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.entity.ProductCategory;
import com.ecommerce.product.entity.ProductStatus;
import com.ecommerce.product.exception.ProductNotFoundException;
import com.ecommerce.product.repository.ProductRepository;
import com.ecommerce.product.service.ProductService;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductServiceTests {

	@Test
	void createsAvailableProduct() {
		ProductService productService = new ProductService(repositoryReturning(Optional.empty()));
		UUID sellerId = UUID.randomUUID();
		CreateProductRequest request = new CreateProductRequest(
				"SKU-1000",
				sellerId,
				"Wireless Headphones",
				ProductCategory.ELECTRONICS,
				new BigDecimal("149.99"),
				LocalDate.of(2026, 9, 15),
				LocalDate.of(2027, 9, 15)
		);

		ProductResponse response = productService.createProduct(request);

		assertEquals(request.sku(), response.sku());
		assertEquals(request.sellerId(), response.sellerId());
		assertEquals(request.name(), response.name());
		assertEquals(request.category(), response.category());
		assertEquals(request.price(), response.price());
		assertEquals(request.availableFrom(), response.availableFrom());
		assertEquals(request.availableUntil(), response.availableUntil());
		assertEquals(ProductStatus.AVAILABLE, response.status());
	}

	@Test
	void throwsWhenProductDoesNotExist() {
		UUID productId = UUID.randomUUID();
		ProductService productService = new ProductService(repositoryReturning(Optional.empty()));

		ProductNotFoundException exception = assertThrows(
				ProductNotFoundException.class,
				() -> productService.getProductById(productId)
		);

		assertEquals("Product not found with id: " + productId, exception.getMessage());
	}

	private ProductRepository repositoryReturning(Optional<Product> product) {
		return (ProductRepository) Proxy.newProxyInstance(
				ProductRepository.class.getClassLoader(),
				new Class<?>[]{ProductRepository.class},
				(proxy, method, arguments) -> switch (method.getName()) {
					case "save" -> arguments[0];
					case "findById" -> product;
					default -> throw new UnsupportedOperationException(method.getName());
				}
		);
	}
}
