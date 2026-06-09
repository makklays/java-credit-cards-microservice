package com.techmatrix18.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a read model for card transactions in a CQRS architecture.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @since 04.06.2026
 * @version 0.0.1
 */
@Table("card_transactions_cqrs_read")
public class CardTransactionCqrsRead {

    @Id
    @Column("card_transaction_id")
    private Long transactionId;

    @Column("credit_card_id")
    private Long creditCardId;

    @Column("merchant_id")
    private Long merchantId;

    @Column("operation_type")
    private String operationType;

    @Column("amount")
    private BigDecimal amount;

    @Column("fee_amount")
    private BigDecimal feeAmount;

    @Column("loyalty_points")
    private BigDecimal loyaltyPoints;

    @Column("currency_code")
    private String currencyCode;

    @Column("status")
    private String status;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private boolean isNew = false;

    // --- Getters and Setters ---

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

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

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isNew() { return isNew; }
    public void setNew(boolean isNew) { this.isNew = isNew; }

    // --- Equals, HashCode, and ToString ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardTransactionCqrsRead that)) return false;

        return (getTransactionId() != null ? getTransactionId().equals(that.getTransactionId()) : that.getTransactionId() == null) &&
            (getCreditCardId() != null ? getCreditCardId().equals(that.getCreditCardId()) : that.getCreditCardId() == null) &&
            (getMerchantId() != null ? getMerchantId().equals(that.getMerchantId()) : that.getMerchantId() == null) &&
            (getOperationType() != null ? getOperationType().equals(that.getOperationType()) : that.getOperationType() == null) &&
            (getAmount() != null ? getAmount().equals(that.getAmount()) : that.getAmount() == null) &&
            (getFeeAmount() != null ? getFeeAmount().equals(that.getFeeAmount()) : that.getFeeAmount() == null) &&
            (getLoyaltyPoints() != null ? getLoyaltyPoints().equals(that.getLoyaltyPoints()) : that.getLoyaltyPoints() == null) &&
            (getCurrencyCode() != null ? getCurrencyCode().equals(that.getCurrencyCode()) : that.getCurrencyCode() == null) &&
            (getStatus() != null ? getStatus().equals(that.getStatus()) : that.getStatus() == null) &&
            (getCreatedAt() != null ? getCreatedAt().equals(that.getCreatedAt()) : that.getCreatedAt() == null) &&
            (getUpdatedAt() != null ? getUpdatedAt().equals(that.getUpdatedAt()) : that.getUpdatedAt() == null);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (transactionId != null ? transactionId.hashCode() : 0);
        result = 31 * result + (creditCardId != null ? creditCardId.hashCode() : 0);
        result = 31 * result + (merchantId != null ? merchantId.hashCode() : 0);
        result = 31 * result + (operationType != null ? operationType.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (feeAmount != null ? feeAmount.hashCode() : 0);
        result = 31 * result + (loyaltyPoints != null ? loyaltyPoints.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "CardTransactionCqrsRead {" +
            "transactionId=" + transactionId +
            ", creditCardId=" + creditCardId +
            ", merchantId=" + merchantId +
            ", operationType='" + operationType + '\'' +
            ", amount=" + amount +
            ", feeAmount=" + feeAmount +
            ", loyaltyPoints=" + loyaltyPoints +
            ", currencyCode='" + currencyCode + '\'' +
            ", status='" + status + '\'' +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}

