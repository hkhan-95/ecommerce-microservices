package com.ecommerce.orderactivity.repository;

import com.ecommerce.orderactivity.event.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OrderActivityRepository {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public OrderActivityRepository(
            DynamoDbClient dynamoDbClient,
            @Value("${app.dynamodb.table-name}") String tableName
    ) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
    }

    public void save(OrderCreatedEvent event) {

        String eventKey =
                event.occurredAt().toString()
                        + "#"
                        + event.eventId();

        Map<String, AttributeValue> item = new HashMap<>();

        item.put(
                "orderId",
                AttributeValue.fromS(event.orderId().toString())
        );

        item.put(
                "eventKey",
                AttributeValue.fromS(eventKey)
        );

        item.put(
                "eventId",
                AttributeValue.fromS(event.eventId().toString())
        );

        item.put(
                "eventType",
                AttributeValue.fromS(event.eventType())
        );

        item.put(
                "productId",
                AttributeValue.fromS(event.productId().toString())
        );

        item.put(
                "customerId",
                AttributeValue.fromS(event.customerId().toString())
        );

        item.put(
                "totalAmount",
                AttributeValue.fromN(
                        event.totalAmount().toString()
                )
        );

        item.put(
                "occurredAt",
                AttributeValue.fromS(event.occurredAt().toString())
        );

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
    }
}
