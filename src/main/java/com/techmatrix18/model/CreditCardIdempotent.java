package com.techmatrix18.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Model class representing a Credit Card Idempotent entity.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 01.06.2026
 */
@Table("credit_cards_idempotent")
public class CreditCardIdempotent implements Persistable<String> {

    @Id
    @Column("idempotency_key")
    private String idempotencyKey;

    @Column("response_body")
    private String responseBody;

    @Column("status")
    private String status;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Transient
    private boolean isNew = true; // Управляет выбором INSERT вместо UPDATE

    // --- Getters and Setters ---

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public String getResponseBody() { return responseBody; }
    public void setResponseBody(String responseBody) { this.responseBody = responseBody; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public void setNew(boolean isNew) { this.isNew = isNew; }

    // --- Реализация интерфейса Persistable ---
    @Override
    public String getId() { return idempotencyKey; }

    @Override
    public boolean isNew() { return isNew; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCardIdempotent that)) return false;
        return Objects.equals(getIdempotencyKey(), that.getIdempotencyKey()) &&
            Objects.equals(getResponseBody(), that.getResponseBody()) &&
            Objects.equals(getStatus(), that.getStatus());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (idempotencyKey != null ? idempotencyKey.hashCode() : 0);
        result = 31 * result + (responseBody != null ? responseBody.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "CreditCardIdempotent {" +
            "idempotencyKey='" + idempotencyKey + '\'' +
            ", responseBody='" + responseBody + '\'' +
            ", status='" + status + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}

