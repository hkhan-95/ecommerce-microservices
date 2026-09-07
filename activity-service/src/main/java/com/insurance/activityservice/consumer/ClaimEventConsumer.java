package com.insurance.activityservice.consumer;

import com.insurance.activityservice.event.ClaimCreatedEvent;
import com.insurance.activityservice.repository.ClaimActivityRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ClaimEventConsumer {

    private final ClaimActivityRepository repository;

    public ClaimEventConsumer(
            ClaimActivityRepository repository
    ) {
        this.repository = repository;
    }

    @KafkaListener(
            topics = "claim-events",
            groupId = "activity-service-v2"
    )
    public void consumeClaimCreated(
            ClaimCreatedEvent event
    ){
        System.out.println("Activity Service received claim:" + event.claimId());

        repository.save(event);

        System.out.println("Activity persisted to DynamoDB");
    }
}
