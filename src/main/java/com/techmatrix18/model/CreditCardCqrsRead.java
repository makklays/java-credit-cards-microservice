package com.techmatrix18.model;

import com.techmatrix18.enums.CreditCardStatus;
import com.techmatrix18.enums.CreditCardType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Model class representing a Credit Card CQRS Read  entity.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 01.06.2026
 */
@Table("credit_cards_cqrs_read")
public class CreditCardCqrsRead implements Persistable<Long> {

    @Id
    @Column("credit_card_id")
    private Long cardId;

    @Column("user_id")
    private Long userId;

    @Column("masked_card_number")
    private String maskedCardNumber;

    @Column("balance")
    private BigDecimal balance;

    @Column("credit_limit")
    private BigDecimal creditLimit;

    @Column("currency_code")
    private String currencyCode;

    private CreditCardStatus status = CreditCardStatus.EXPIRED;

    @Column("type")
    private CreditCardType type = CreditCardType.VISA;

    @Column("bank_name")
    private String bankName;

    @Column("is_blocked")
    private Boolean isBlocked;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Transient
    private boolean isNew = true; // Управляет логикой сохранения записи

    // --- Constructors (Builder) ---

    public CreditCardCqrsRead() {}

    private CreditCardCqrsRead(Builder builder) {
        this.cardId = builder.cardId;
        this.userId = builder.userId;
        this.maskedCardNumber = builder.maskedCardNumber;
        this.balance = builder.balance;
        this.creditLimit = builder.creditLimit;
        this.currencyCode = builder.currencyCode;
        this.status = builder.status;
        this.type = builder.type;
        this.bankName = builder.bankName;
        this.isBlocked = builder.isBlocked;
        this.updatedAt = builder.updatedAt;
        this.isNew = builder.isNew;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long cardId;
        private Long userId;
        private String maskedCardNumber;
        private BigDecimal balance;
        private BigDecimal creditLimit;
        private String currencyCode;
        private CreditCardStatus status = CreditCardStatus.EXPIRED; // Дефолтное значение из вашей модели
        private CreditCardType type = CreditCardType.VISA;          // Дефолтное значение из вашей модели
        private String bankName;
        private Boolean isBlocked;
        private LocalDateTime updatedAt;
        private boolean isNew = true;                               // Дефолтное значение из вашей модели

        public Builder cardId(Long cardId) {
            this.cardId = cardId;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder maskedCardNumber(String maskedCardNumber) {
            this.maskedCardNumber = maskedCardNumber;
            return this;
        }

        public Builder balance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        public Builder creditLimit(BigDecimal creditLimit) {
            this.creditLimit = creditLimit;
            return this;
        }

        public Builder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public Builder status(CreditCardStatus status) {
            this.status = status;
            return this;
        }

        public Builder type(CreditCardType type) {
            this.type = type;
            return this;
        }

        public Builder bankName(String bankName) {
            this.bankName = bankName;
            return this;
        }

        public Builder isBlocked(Boolean isBlocked) {
            this.isBlocked = isBlocked;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder isNew(boolean isNew) {
            this.isNew = isNew;
            return this;
        }

        public CreditCardCqrsRead build() {
            return new CreditCardCqrsRead(this);
        }
    }

    // --- Getters and Setters ---

    public Long getCardId() { return cardId; }
    public void setCardId(Long cardId) { this.cardId = cardId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMaskedCardNumber() { return maskedCardNumber; }
    public void setMaskedCardNumber(String maskedCardNumber) { this.maskedCardNumber = maskedCardNumber; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public BigDecimal getCreditLimit() { return creditLimit; }
    public void setCreditLimit(BigDecimal creditLimit) { this.creditLimit = creditLimit; }

    public String getCurrencyCode() { return currencyCode; }
    public void setCurrencyCode(String currencyCode) { this.currencyCode = currencyCode; }

    public CreditCardStatus getStatus() { return status; }
    public void setStatus(CreditCardStatus status) { this.status = status; }

    public CreditCardType getType() { return type; }
    public void setType(CreditCardType type) { this.type = type; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public Boolean getIsBlocked() { return isBlocked; }
    public void setIsBlocked(Boolean isBlocked) { this.isBlocked = isBlocked; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public void setNew(boolean isNew) { this.isNew = isNew; }

    // --- Реализация интерфейса Persistable ---

    @Override
    public Long getId() { return cardId; }

    @Override
    public boolean isNew() { return isNew; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCardCqrsRead that)) return false;
        return Objects.equals(getCardId(), that.getCardId()) &&
            Objects.equals(getUserId(), that.getUserId()) &&
            Objects.equals(getMaskedCardNumber(), that.getMaskedCardNumber()) &&
            Objects.equals(getBalance(), that.getBalance()) &&
            Objects.equals(getCreditLimit(), that.getCreditLimit()) &&
            Objects.equals(getCurrencyCode(), that.getCurrencyCode()) &&
            getStatus() == that.getStatus() &&
            getType() == that.getType() &&
            Objects.equals(getBankName(), that.getBankName()) &&
            Objects.equals(getIsBlocked(), that.getIsBlocked());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (cardId != null ? cardId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (maskedCardNumber != null ? maskedCardNumber.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (creditLimit != null ? creditLimit.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (bankName != null ? bankName.hashCode() : 0);
        result = 31 * result + (isBlocked != null ? isBlocked.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "CreditCardCqrsRead {" +
            "cardId=" + cardId +
            ", userId=" + userId +
            ", maskedCardNumber='" + maskedCardNumber + '\'' +
            ", balance=" + balance +
            ", creditLimit=" + creditLimit +
            ", currencyCode='" + currencyCode + '\'' +
            ", status=" + status +
            ", type=" + type +
            ", bankName='" + bankName + '\'' +
            ", isBlocked=" + isBlocked +
            ", updatedAt=" + updatedAt +
            '}';
    }
}

