package com.axisbanking.common.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, BankingEvent> kafkaTemplate;

    public void publish(String topic, String eventType, String entityId,
                        String entityType, String action, Map<String, Object> payload) {
        BankingEvent event = BankingEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(eventType)
                .source(topic)
                .entityId(entityId)
                .entityType(entityType)
                .action(action)
                .payload(payload)
                .timestamp(LocalDateTime.now())
                .correlationId(UUID.randomUUID().toString())
                .build();

        CompletableFuture<SendResult<String, BankingEvent>> future =
                kafkaTemplate.send(topic, entityId, event);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Failed to publish event {} to topic {}: {}", eventType, topic, ex.getMessage());
                publishToDlq(event);
            } else {
                log.info("Published event {} to topic {} partition {} offset {}",
                        eventType, topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            }
        });
    }

    private void publishToDlq(BankingEvent event) {
        try {
            kafkaTemplate.send(KafkaTopics.DLQ_EVENTS, event.getEntityId(), event);
            log.warn("Event {} sent to DLQ", event.getEventId());
        } catch (Exception e) {
            log.error("Failed to send event to DLQ: {}", e.getMessage());
        }
    }
}
