package com.axisbanking.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class BankingEvent implements Serializable {
    private String eventId;
    private String eventType;
    private String source;
    private String entityId;
    private String entityType;
    private String action;
    private Map<String, Object> payload;
    private LocalDateTime timestamp;
    private String correlationId;
}
