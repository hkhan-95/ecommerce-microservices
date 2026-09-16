package com.ecommerce.order;

import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.ProductStatus;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.event.OrderCreatedEvent;
import com.ecommerce.order.exception.GlobalExceptionHandler;
import com.ecommerce.order.exception.ProductNotFoundException;
import com.ecommerce.order.exception.ProductServiceUnavailableException;
import com.ecommerce.order.exception.ProductUnavailableException;
import com.ecommerce.order.producer.OrderEventProducer;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderServiceTests {

	@Test
	void createsPlacedOrderUsingProductPrice() {
		UUID productId = UUID.randomUUID();
		UUID customerId = UUID.randomUUID();
		BigDecimal productPrice = new BigDecimal("149.99");
		ProductClient productClient = id -> product(productId, productPrice, ProductStatus.AVAILABLE);
		RecordingOrderEventProducer eventProducer = new RecordingOrderEventProducer();
		OrderService orderService = new OrderService(orderRepository(), productClient, eventProducer);

		OrderResponse response = orderService.createOrder(
				new CreateOrderRequest(productId, customerId, "Leave at the front desk")
		);

		assertNotNull(response.id());
		assertEquals(productId, response.productId());
		assertEquals(customerId, response.customerId());
		assertEquals("Leave at the front desk", response.orderNotes());
		assertEquals(productPrice, response.totalAmount());
		assertEquals(OrderStatus.PLACED, response.status());
		assertNotNull(eventProducer.publishedEvent);
		assertEquals("ORDER_CREATED", eventProducer.publishedEvent.eventType());
		assertEquals(response.id(), eventProducer.publishedEvent.orderId());
		assertEquals(productPrice, eventProducer.publishedEvent.totalAmount());
	}

	@Test
	void rejectsProductsThatAreNotAvailable() {
		for (ProductStatus status : List.of(ProductStatus.OUT_OF_STOCK, ProductStatus.DISCONTINUED)) {
			UUID productId = UUID.randomUUID();
			ProductClient productClient = id -> product(productId, new BigDecimal("49.99"), status);
			OrderService orderService = new OrderService(
					orderRepository(),
					productClient,
					new RecordingOrderEventProducer()
			);

			ProductUnavailableException exception = assertThrows(
					ProductUnavailableException.class,
					() -> orderService.createOrder(
							new CreateOrderRequest(productId, UUID.randomUUID(), "No special instructions")
					)
			);

			assertEquals(
					"Product is not available: " + productId + " (status: " + status + ")",
					exception.getMessage()
			);
		}
	}

	@Test
	void productClientTargetsProductServiceThroughTheProductsRoute() throws NoSuchMethodException {
		FeignClient feignClient = ProductClient.class.getAnnotation(FeignClient.class);
		GetMapping getMapping = ProductClient.class
				.getMethod("getProductById", UUID.class)
				.getAnnotation(GetMapping.class);

		assertEquals("product-service", feignClient.name());
		assertEquals("/api/v1/products/{id}", getMapping.value()[0]);
	}

	@Test
	void mapsProductFailuresToRequiredHttpStatuses() {
		GlobalExceptionHandler handler = new GlobalExceptionHandler();
		UUID productId = UUID.randomUUID();

		assertEquals(
				HttpStatus.NOT_FOUND,
				handler.handleProductNotFound(new ProductNotFoundException(productId)).getStatusCode()
		);
		assertEquals(
				HttpStatus.CONFLICT,
				handler.handleProductUnavailable(
						new ProductUnavailableException(productId, ProductStatus.OUT_OF_STOCK)
				).getStatusCode()
		);
		assertEquals(
				HttpStatus.SERVICE_UNAVAILABLE,
				handler.handleProductServiceUnavailable(
						new ProductServiceUnavailableException()
				).getStatusCode()
		);
	}

	private ProductResponse product(UUID productId, BigDecimal price, ProductStatus status) {
		return new ProductResponse(
				productId,
				"SKU-1000",
				UUID.randomUUID(),
				"Wireless Headphones",
				"ELECTRONICS",
				price,
				status,
				LocalDate.of(2026, 9, 15),
				LocalDate.of(2027, 9, 15),
				null
		);
	}

	private OrderRepository orderRepository() {
		return (OrderRepository) Proxy.newProxyInstance(
				OrderRepository.class.getClassLoader(),
				new Class<?>[]{OrderRepository.class},
				(proxy, method, arguments) -> switch (method.getName()) {
					case "save" -> {
						Order order = (Order) arguments[0];
						order.setId(UUID.randomUUID());
						yield order;
					}
					case "findById" -> Optional.empty();
					case "findAll" -> List.of();
					default -> throw new UnsupportedOperationException(method.getName());
				}
		);
	}

	private static class RecordingOrderEventProducer extends OrderEventProducer {
		private OrderCreatedEvent publishedEvent;

		private RecordingOrderEventProducer() {
			super(null);
		}

		@Override
		public void publishOrderCreated(String orderId, OrderCreatedEvent event) {
			publishedEvent = event;
		}
	}
}
