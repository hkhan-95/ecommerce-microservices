package com.insurance.claims_service.producer;

import com.insurance.claims_service.event.ClaimCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClaimEventProducer {
    private static final String TOPIC = "claim-events";

    private final KafkaTemplate<String, ClaimCreatedEvent> kafkaTemplate;

    public ClaimEventProducer(
            KafkaTemplate<String, ClaimCreatedEvent> kafkaTemplate
    ){
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishClaimCreated(
            String claimId,
            ClaimCreatedEvent event
    ){
        kafkaTemplate.send(
                TOPIC,
                claimId,
                event
                ).whenComplete((result, ex) -> {

                    if (ex != null){
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
