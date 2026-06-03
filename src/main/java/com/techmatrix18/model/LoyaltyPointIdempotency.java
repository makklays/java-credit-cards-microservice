package com.techmatrix18.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

/**
 * Model class representing the idempotency record for loyalty points operations.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 03.06.2026
 * @version 0.0.1
 */
@Table("loyalty_points_idempotency")
public class LoyaltyPointIdempotency {

    @Id
    @Column("idempotency_key")
    private String idempotencyKey;

    @Column("response_body")
    private String responseBody;

    @Column("status")
    private String status;

    @Column("created_at")
    private LocalDateTime createdAt;

    // Технический флаг для Spring Data R2DBC, чтобы отличать INSERT от UPDATE
    @Transient
    private boolean isNew = false;

    // --- Getters and Setters ---

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }

    // --- Equals, HashCode, and ToString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoyaltyPointIdempotency that)) return false;

        return (getIdempotencyKey() != null ? getIdempotencyKey().equals(that.getIdempotencyKey()) : that.getIdempotencyKey() == null) &&
            (getResponseBody() != null ? getResponseBody().equals(that.getResponseBody()) : that.getResponseBody() == null) &&
            (getStatus() != null ? getStatus().equals(that.getStatus()) : that.getStatus() == null) &&
            (getCreatedAt() != null ? getCreatedAt().equals(that.getCreatedAt()) : that.getCreatedAt() == null);
    }

    @Override
    public int hashCode() {
        int result = 17; // Стартовое простое число по вашему шаблону
        result = 31 * result + (idempotencyKey != null ? idempotencyKey.hashCode() : 0);
        result = 31 * result + (responseBody != null ? responseBody.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "LoyaltyPointIdempotency {" +
            "idempotencyKey='" + idempotencyKey + '\'' +
            ", responseBody='" + responseBody + '\'' +
            ", status='" + status + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}

