package com.ecommerce.orderactivity.consumer;

import com.ecommerce.orderactivity.event.OrderCreatedEvent;
import com.ecommerce.orderactivity.repository.OrderActivityRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private final OrderActivityRepository repository;

    public OrderEventConsumer(
            OrderActivityRepository repository
    ) {
        this.repository = repository;
    }

    @KafkaListener(
            topics = "order-events",
            groupId = "order-activity-service"
    )
    public void consumeOrderCreated(
            OrderCreatedEvent event
    ) {
        System.out.println("Order Activity Service received order: " + event.orderId());

        repository.save(event);

        System.out.println("Activity persisted to DynamoDB");
    }
}
