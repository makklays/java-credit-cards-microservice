package com.techmatrix18.model;

import com.techmatrix18.enums.OperationType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing fees/commissions for credit card operations.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Table("operation_fees")
public class OperationFee {

    @Id
    @Column("id")
    private Long id;

    @Column("operation_type")
    private OperationType operationType = OperationType.DEPOSIT; // example: DEPOSIT, PAYMENT, WITHDRAWAL, TRANSFER

    @Column("description")
    private String description; // Description of the operation fee

    @Column("fee_percentage")
    private BigDecimal feePercentage; // example 1.5% -> 0.015

    @Column("fee_fixed")
    private BigDecimal feeFixed; // Fixed amount, for example 5.00

    @Column("currency_code")
    private String currencyCode; // Currency code for the fixed fee, e.g., "USD", "EUR"

    @Column("min_fee")
    private BigDecimal minFee; // Minimum fee amount

    @Column("max_fee")
    private BigDecimal maxFee; // Maximum fee amount

    @Column("active")
    private boolean active; // Is this fee active (1) or inactive (0)

    @Column("valid_from")
    private LocalDateTime validFrom; // Start date of validity

    @Column("valid_to")
    private LocalDateTime validTo; // Finish date of validity

    @Column("loyalty_percentage")
    private BigDecimal loyaltyPercentage; // example 1% -> 0.01 points for loyalty program

    @Column("loyalty_fixed")
    private BigDecimal loyaltyFixed; // Fixed points, for example 2.00

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OperationType getOperationType() { return operationType; }
    public void setOperationType(OperationType operationType) { this.operationType = operationType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getFeePercentage() { return feePercentage; }
    public void setFeePercentage(BigDecimal feePercentage) { this.feePercentage = feePercentage; }

    public BigDecimal getFeeFixed() { return feeFixed; }
    public void setFeeFixed(BigDecimal feeFixed) { this.feeFixed = feeFixed; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public BigDecimal getMinFee() { return minFee; }
    public void setMinFee(BigDecimal minFee) { this.minFee = minFee; }

    public BigDecimal getMaxFee() { return maxFee; }
    public void setMaxFee(BigDecimal maxFee) { this.maxFee = maxFee; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }

    public LocalDateTime getValidTo() { return validTo; }
    public void setValidTo(LocalDateTime validTo) { this.validTo = validTo; }

    public BigDecimal getLoyaltyPercentage() { return feePercentage; }
    public void setLoyaltyPercentage(BigDecimal feePercentage) { this.feePercentage = feePercentage; }

    public BigDecimal getLoyaltyFixed() { return loyaltyFixed; }
    public void setLoyaltyFixed(BigDecimal loyaltyFixed) { this.loyaltyFixed = loyaltyFixed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperationFee that)) return false;
        return isActive() == that.isActive() && getId().equals(that.getId()) &&
                getOperationType() == that.getOperationType() && getDescription().equals(that.getDescription()) &&
                getFeePercentage().equals(that.getFeePercentage()) && getFeeFixed().equals(that.getFeeFixed()) &&
                getCurrencyCode().equals(that.getCurrencyCode()) && getMinFee().equals(that.getMinFee()) &&
                getMaxFee().equals(that.getMaxFee()) && getValidFrom().equals(that.getValidFrom()) &&
                getValidTo().equals(that.getValidTo()) && getLoyaltyPercentage().equals(that.getLoyaltyPercentage()) &&
                getLoyaltyFixed().equals(that.getLoyaltyFixed());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        result = 31 * result + (getOperationType() != null ? getOperationType().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getFeePercentage() != null ? getFeePercentage().hashCode() : 0);
        result = 31 * result + (getFeeFixed() != null ? getFeeFixed().hashCode() : 0);
        result = 31 * result + (getCurrencyCode() != null ? getCurrencyCode().hashCode() : 0);
        result = 31 * result + (getMinFee() != null ? getMinFee().hashCode() : 0);
        result = 31 * result + (getMaxFee() != null ? getMaxFee().hashCode() : 0);
        result = 31 * result + (getValidFrom() != null ? getValidFrom().hashCode() : 0);
        result = 31 * result + (getValidTo() != null ? getValidTo().hashCode() : 0);
        result = 31 * result + (getLoyaltyPercentage() != null ? getLoyaltyPercentage().hashCode() : 0);
        result = 31 * result + (getLoyaltyFixed() != null ? getLoyaltyFixed().hashCode() : 0);
        result = 31 * result + Boolean.hashCode(isActive());

        return result;
    }

    @Override
    public String toString() {
        return "OperationFee {" +
            "id=" + id +
            ", operationType=" + operationType +
            ", description='" + description + '\'' +
            ", feePercentage=" + feePercentage +
            ", feeFixed=" + feeFixed +
            ", currencyCode='" + currencyCode + '\'' +
            ", minFee=" + minFee +
            ", maxFee=" + maxFee +
            ", active=" + active +
            ", validFrom=" + validFrom +
            ", validTo=" + validTo +
            ", loyaltyPercentage=" + loyaltyPercentage +
            ", loyaltyFixed=" + loyaltyFixed +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}

