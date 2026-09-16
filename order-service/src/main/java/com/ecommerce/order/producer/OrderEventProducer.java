package com.ecommerce.order.producer;

import com.ecommerce.order.event.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventProducer {
    private static final String TOPIC = "order-events";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public OrderEventProducer(
            KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreated(
            String orderId,
            OrderCreatedEvent event
    ) {
        kafkaTemplate.send(
                TOPIC,
                orderId,
                event
                ).whenComplete((result, ex) -> {

                    if (ex != null) {
                        System.out.println(
                                "Kafka publish FAILED: " + ex.getMessage()
                        );
                    }
                    else {
                        System.out.println(
                                " Kafka publish SUCCESS \n"
                                + " topic= " + result.getRecordMetadata().topic()
                                + "\n partition= " + result.getRecordMetadata().partition()
                                + "\n offset= " + result.getRecordMetadata().offset()
                        );
                    }
        });
    }
}
