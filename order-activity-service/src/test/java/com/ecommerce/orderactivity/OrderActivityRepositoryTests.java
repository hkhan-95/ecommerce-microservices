package com.ecommerce.orderactivity;

import com.ecommerce.orderactivity.consumer.OrderEventConsumer;
import com.ecommerce.orderactivity.event.OrderCreatedEvent;
import com.ecommerce.orderactivity.repository.OrderActivityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.annotation.KafkaListener;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.lang.reflect.Proxy;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderActivityRepositoryTests {

    @Test
    void persistsApprovedOrderEventUsingExistingEventKeyStrategy() {
        AtomicReference<PutItemRequest> capturedRequest = new AtomicReference<>();
        DynamoDbClient dynamoDbClient = capturingDynamoDbClient(capturedRequest);
        OrderActivityRepository repository = new OrderActivityRepository(
                dynamoDbClient,
                "order-activity"
        );

        UUID eventId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        LocalDateTime occurredAt = LocalDateTime.of(2026, 9, 16, 10, 30);
        OrderCreatedEvent event = new OrderCreatedEvent(
                eventId,
                "ORDER_CREATED",
                orderId,
                productId,
                customerId,
                new BigDecimal("149.99"),
                occurredAt
        );

        repository.save(event);

        PutItemRequest request = capturedRequest.get();
        assertNotNull(request);
        assertEquals("order-activity", request.tableName());
        assertEquals(orderId.toString(), request.item().get("orderId").s());
        assertEquals(occurredAt + "#" + eventId, request.item().get("eventKey").s());
        assertEquals(eventId.toString(), request.item().get("eventId").s());
        assertEquals("ORDER_CREATED", request.item().get("eventType").s());
        assertEquals(productId.toString(), request.item().get("productId").s());
        assertEquals(customerId.toString(), request.item().get("customerId").s());
        assertEquals("149.99", request.item().get("totalAmount").n());
        assertEquals(occurredAt.toString(), request.item().get("occurredAt").s());
        assertEquals(8, request.item().size());
    }

    @Test
    void listensToOrderEventsWithOrderActivityConsumerGroup() throws NoSuchMethodException {
        KafkaListener listener = OrderEventConsumer.class
                .getMethod("consumeOrderCreated", OrderCreatedEvent.class)
                .getAnnotation(KafkaListener.class);

        assertArrayEquals(new String[]{"order-events"}, listener.topics());
        assertEquals("order-activity-service", listener.groupId());
    }

    private DynamoDbClient capturingDynamoDbClient(
            AtomicReference<PutItemRequest> capturedRequest
    ) {
        return (DynamoDbClient) Proxy.newProxyInstance(
                DynamoDbClient.class.getClassLoader(),
                new Class<?>[]{DynamoDbClient.class},
                (proxy, method, arguments) -> {
                    if ("putItem".equals(method.getName())) {
                        capturedRequest.set((PutItemRequest) arguments[0]);
                        return PutItemResponse.builder().build();
                    }
                    throw new UnsupportedOperationException(method.getName());
                }
        );
    }
}
