package com.insurance.activityservice.repository;

import com.insurance.activityservice.event.ClaimCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.HashMap;
import java.util.Map;

@Repository
public class ClaimActivityRepository {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName;

    public ClaimActivityRepository(
            DynamoDbClient dynamoDbClient,
            @Value("${app.dynamodb.table-name}") String tableName
    ) {
        this.dynamoDbClient = dynamoDbClient;
        this.tableName = tableName;
    }

    public void save(ClaimCreatedEvent event) {

        String eventKey =
                event.occurredAt().toString()
                        + "#"
                        + event.eventId();

        Map<String, AttributeValue> item = new HashMap<>();

        item.put(
                "claimId",
                AttributeValue.fromS(event.claimId().toString())
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
                "policyId",
                AttributeValue.fromS(event.policyId().toString())
        );

        item.put(
                "customerId",
                AttributeValue.fromS(event.customerId().toString())
        );

        item.put(
                "claimType",
                AttributeValue.fromS(event.claimType())
        );

        item.put(
                "amountRequested",
                AttributeValue.fromN(
                        event.amountRequested().toString()
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
