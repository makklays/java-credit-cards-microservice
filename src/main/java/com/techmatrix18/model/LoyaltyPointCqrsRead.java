package com.techmatrix18.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

/**
 * Model class representing the read model for Loyalty Points in a CQRS architecture.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 03.06.2026
 * @version 0.0.1
 */
@Table("loyalty_points_cqrs_read")
public class LoyaltyPointCqrsRead {

    @Id
    @Column("credit_card_id")
    private Long creditCardId;

    @Column("points_balance")
    private Long pointsBalance;

    @Column("expiration_date")
    private LocalDateTime expirationDate;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    // Техническое поле для Spring Data R2DBC, чтобы отличать INSERT от UPDATE
    @Transient
    private boolean isNew = false;

    // --- Getters and Setters ---

    public Long getCreditCardId() { return creditCardId; }
    public void setCreditCardId(Long creditCardId) { this.creditCardId = creditCardId; }

    public Long getPointsBalance() { return pointsBalance; }
    public void setPointsBalance(Long pointsBalance) { this.pointsBalance = pointsBalance; }

    public LocalDateTime getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }

    // --- Equals, HashCode, and ToString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoyaltyPointCqrsRead that)) return false;

        return (getCreditCardId() != null ? getCreditCardId().equals(that.getCreditCardId()) : that.getCreditCardId() == null) &&
            (getPointsBalance() != null ? getPointsBalance().equals(that.getPointsBalance()) : that.getPointsBalance() == null) &&
            (getExpirationDate() != null ? getExpirationDate().equals(that.getExpirationDate()) : that.getExpirationDate() == null) &&
            (getUpdatedAt() != null ? getUpdatedAt().equals(that.getUpdatedAt()) : that.getUpdatedAt() == null);
    }

    @Override
    public int hashCode() {
        int result = 17; // Стартовое простое число по вашему шаблону
        result = 31 * result + (creditCardId != null ? creditCardId.hashCode() : 0);
        result = 31 * result + (pointsBalance != null ? pointsBalance.hashCode() : 0);
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "LoyaltyPointCqrsRead {" +
            "creditCardId=" + creditCardId +
            ", pointsBalance=" + pointsBalance +
            ", expirationDate=" + expirationDate +
            ", updatedAt=" + updatedAt +
            '}';
    }
}

