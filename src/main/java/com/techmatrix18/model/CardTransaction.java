package com.techmatrix18.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entity representing a transaction/operation on a credit card.
 * Stores history of all operations: deposits, withdrawals, transfers, payment with fees and loyalty points.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Table("card_transactions")
public class CardTransaction {

    @Id
    @Column("id")
    private Long id;

    @Column("credit_card_id")
    private Long creditCardId;    // CreditCard

    @Column("merchant_id")
    private Long merchantId;      // Merchant

    @Column("operation_type")
    private String operationType; // WITHDRAWAL, DEPOSIT, TRANSFER, PAYMENT

    @Column("amount")
    private BigDecimal amount;    // Сумма операции

    @Column("fee_amount")
    private BigDecimal feeAmount; // Сумма комиссии (если есть)

    @Column("loyalty_points")
    private BigDecimal loyaltyPoints; // Начисленные бонусные баллы (если есть)

    @Column("currency_id")
    private Long currencyId; // Валюта

    @Column("currency_code")
    private String currencyCode; // Валюта операции, например USD, EUR

    @Column("status")
    private String status; // PENDING, COMPLETED, FAILED, PAYMENT

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCreditCardId() { return creditCardId; }
    public void setCreditCardId(Long creditCardId) { this.creditCardId = creditCardId; }

    public Long getMerchantId() { return merchantId; }
    public void setMerchantId(Long merchantId) { this.merchantId = merchantId; }

    public String getOperationType() { return operationType; }
    public void setOperationType(String operationType) { this.operationType = operationType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getFeeAmount() { return feeAmount; }
    public void setFeeAmount(BigDecimal feeAmount) { this.feeAmount = feeAmount; }

    public BigDecimal getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(BigDecimal loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }

    public Long getCurrencyId() { return currencyId; }
    public void setCurrencyId(Long currencyId) { this.currencyId = currencyId; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyId) { this.currencyCode = currencyCode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardTransaction that)) return false;
        return getId().equals(that.getId()) && getCreditCardId().equals(that.getCreditCardId()) &&
                getMerchantId().equals(that.getMerchantId()) && getOperationType().equals(that.getOperationType()) &&
                getAmount().equals(that.getAmount()) && getFeeAmount().equals(that.getFeeAmount()) &&
                getLoyaltyPoints().equals(that.getLoyaltyPoints()) && getCurrencyId().equals(that.getCurrencyId()) &&
                getCurrencyCode().equals(that.getCurrencyCode()) && getStatus().equals(that.getStatus()) &&
                Objects.equals(getCreatedAt(), that.getCreatedAt()) && Objects.equals(getUpdatedAt(), that.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (merchantId != null ? merchantId.hashCode() : 0);
        result = 31 * result + (creditCardId != null ? creditCardId.hashCode() : 0);
        result = 31 * result + (operationType != null ? operationType.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (feeAmount != null ? feeAmount.hashCode() : 0);
        result = 31 * result + (loyaltyPoints != null ? loyaltyPoints.hashCode() : 0);
        result = 31 * result + (currencyId != null ? currencyId.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CardTransaction {" +
            "id=" + id +
            ", creditCardId=" + creditCardId +
            ", merchantId=" + merchantId +
            ", operationType='" + operationType + '\'' +
            ", amount=" + amount +
            ", feeAmount=" + feeAmount +
            ", loyaltyPoints=" + loyaltyPoints +
            ", currencyId=" + currencyId +
            ", currencyCode='" + currencyCode + '\'' +
            ", status='" + status + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}

