package com.techmatrix18.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Entity representing the loyalty program and loyalty/reward points for a credit card.
 * Tracks accumulated points and last update time.
 *
 * За что начисляются баллы:
 * Покупки по карте – процент от суммы покупки (например, 1% или 2%).
 * Пополнение счета/депозит – иногда начисляют бонус за внесение средств.
 * Переводы или оплата счетов – может быть бонус, если банк стимулирует активность.
 * Специальные акции – бонус за определённые категории расходов: рестораны, путешествия, топливо.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Table("loyalty_points")
public class LoyaltyPoint {
    @Id
    @Column("id")
    private Long id;

    @Column("credit_card_id")
    private Long creditCardId;  // CreditCard

    @Column("points_balance")
    private Long pointsBalance; // Количество накопленных бонусных баллов

    @Column("last_updated")
    private LocalDateTime lastUpdated; // Время последнего обновления баллов

    @Column("expiration_date")
    private LocalDateTime expirationDate; // Дата истечения бонусных баллов

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCreditCardId() { return creditCardId; }
    public void setCreditCardId(Long creditCardId) { this.creditCardId = creditCardId; }

    public Long getPointsBalance() { return pointsBalance; }
    public void setPointsBalance(Long pointsBalance) { this.pointsBalance = pointsBalance; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    public LocalDateTime getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoyaltyPoint that)) return false;
        return getId().equals(that.getId()) && getCreditCardId().equals(that.getCreditCardId()) &&
                getPointsBalance().equals(that.getPointsBalance());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (creditCardId != null ? creditCardId.hashCode() : 0);
        result = 31 * result + (pointsBalance != null ? pointsBalance.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LoyaltyPoint {" +
            "id=" + id +
            ", creditCardId=" + creditCardId +
            ", pointsBalance=" + pointsBalance +
            ", lastUpdated=" + lastUpdated +
            ", expirationDate=" + expirationDate +
            '}';
    }
}

