package com.ecommerce.order.service;

import com.ecommerce.order.client.ProductClient;
import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.dto.ProductResponse;
import com.ecommerce.order.dto.ProductStatus;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderStatus;
import com.ecommerce.order.event.OrderCreatedEvent;
import com.ecommerce.order.exception.ProductNotFoundException;
import com.ecommerce.order.exception.ProductServiceUnavailableException;
import com.ecommerce.order.exception.ProductUnavailableException;
import com.ecommerce.order.mapper.OrderMapper;
import com.ecommerce.order.producer.OrderEventProducer;
import com.ecommerce.order.repository.OrderRepository;
import feign.FeignException;
import feign.RetryableException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderEventProducer orderEventProducer;

    public OrderService(
            OrderRepository orderRepository,
            ProductClient productClient,
            OrderEventProducer orderEventProducer
    ) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.orderEventProducer = orderEventProducer;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {

        ProductResponse product;

        try {
            product = productClient.getProductById(request.productId());
        } catch (FeignException.NotFound ex) {
            throw new ProductNotFoundException(request.productId());
        } catch (RetryableException | FeignException.ServiceUnavailable ex) {
            throw new ProductServiceUnavailableException();
        }

        if (product.status() != ProductStatus.AVAILABLE) {
            throw new ProductUnavailableException(request.productId(), product.status());
        }

        Order order = OrderMapper.toEntity(request);
        order.setTotalAmount(product.price());
        order.setStatus(OrderStatus.PLACED);
        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(),
                "ORDER_CREATED",
                savedOrder.getId(),
                savedOrder.getProductId(),
                savedOrder.getCustomerId(),
                savedOrder.getTotalAmount(),
                LocalDateTime.now()
        );

        orderEventProducer.publishOrderCreated(
                savedOrder.getId().toString(),
                event
        );

        return OrderMapper.toResponse(savedOrder);
    }

    public OrderResponse getOrderById(UUID id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Order not found with id: " + id)
                );

        return OrderMapper.toResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }
}
