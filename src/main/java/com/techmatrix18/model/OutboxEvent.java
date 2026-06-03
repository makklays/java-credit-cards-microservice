package com.techmatrix18.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OutboxEvent entity for storing events in the outbox table.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 03.06.2026
 * @version 0.0.2
 */
public class OutboxEvent {

    @Id
    private UUID id;

    @Column("aggregate_type")
    private String aggregateType;

    @Column("aggregate_id")
    private String aggregateId;

    @Column("event_type")
    private String eventType;

    @Column("payload")
    private String payload;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("processed")
    private boolean processed;

    @Column("processed_at")
    private LocalDateTime processedAt;

    // --- Getters and Setters ---

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getAggregateType() { return aggregateType; }

    public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }

    public String getAggregateId() { return aggregateId; }

    public void setAggregateId(String aggregateId) { this.aggregateId = aggregateId; }

    public String getEventType() { return eventType; }

    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getPayload() { return payload; }

    public void setPayload(String payload) { this.payload = payload; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isProcessed() { return processed; }

    public void setProcessed(boolean processed) { this.processed = processed; }

    public LocalDateTime getProcessedAt() { return processedAt; }

    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    // --- Equals, HashCode, and ToString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OutboxEvent that)) return false;

        // Безопасная проверка на null с помощью Objects.equals или ручной проверки, как в вашем примере
        return (getId() != null ? getId().equals(that.getId()) : that.getId() == null) &&
            (getAggregateType() != null ? getAggregateType().equals(that.getAggregateType()) : that.getAggregateType() == null) &&
            (getAggregateId() != null ? getAggregateId().equals(that.getAggregateId()) : that.getAggregateId() == null) &&
            (getEventType() != null ? getEventType().equals(that.getEventType()) : that.getEventType() == null) &&
            (getPayload() != null ? getPayload().equals(that.getPayload()) : that.getPayload() == null) &&
            (getCreatedAt() != null ? getCreatedAt().equals(that.getCreatedAt()) : that.getCreatedAt() == null) &&
            isProcessed() == that.isProcessed() &&
            (getProcessedAt() != null ? getProcessedAt().equals(that.getProcessedAt()) : that.getProcessedAt() == null);
    }

    @Override
    public int hashCode() {
        int result = 17; // Стартовое простое число по вашему шаблону
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (aggregateType != null ? aggregateType.hashCode() : 0);
        result = 31 * result + (aggregateId != null ? aggregateId.hashCode() : 0);
        result = 31 * result + (eventType != null ? eventType.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (processed ? 1 : 0);
        result = 31 * result + (processedAt != null ? processedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OutboxEvent {" +
            "id=" + id +
            ", aggregateType='" + aggregateType + '\'' +
            ", aggregateId='" + aggregateId + '\'' +
            ", eventType='" + eventType + '\'' +
            ", payload='" + payload + '\'' +
            ", createdAt=" + createdAt +
            ", processed=" + processed +
            ", processedAt=" + processedAt +
            '}';
    }
}

