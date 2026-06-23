package com.techmatrix18.model;

import com.techmatrix18.enums.CreditCardStatus;
import com.techmatrix18.enums.CreditCardType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Model class representing a Credit Card entity.
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 25.12.2025
 */
@Table(name = "credit_cards")
public class CreditCard {

    @Id
    @Column("id")
    private Long id; // Primary key, auto-generated

    @Column("user_id")
    private Long userId;

    @Column("card_number")
    private String cardNumber;

    @Column("cardholder_name")
    private String cardholderName;

    @Column("expiration_date")
    private LocalDate expirationDate;

    @Column("cvv")
    private String cvv;

    @Column("pin")
    private String pin;

    @Column("balance")
    private BigDecimal balance;

    @Column("credit_limit")
    private BigDecimal creditLimit;

    @Column("currency_code")
    private String currencyCode;

    private CreditCardStatus status = CreditCardStatus.EXPIRED ; // by default EXPIRED

    @Column("type")
    private CreditCardType type = CreditCardType.VISA; // by default VISA

    @Column("bank_name")
    private String bankName;

    @Column("issue_date")
    private LocalDate issueDate;

    @Column("reward_points")
    private Long rewardPoints;

    @Column("interest_rate")
    private BigDecimal interestRate;

    @Column("contactless")
    private Boolean contactless; // 0 or 1

    @Column("is_blocked")
    private Boolean isBlocked;

    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("updated_at")
    private LocalDateTime updatedAt;

    // --- Constructors (Builder) ---

    public CreditCard() {}

    private CreditCard(Builder builder) {
        this.id = builder.id;
        this.userId = builder.userId;
        this.cardNumber = builder.cardNumber;
        this.cardholderName = builder.cardholderName;
        this.expirationDate = builder.expirationDate;
        this.cvv = builder.cvv;
        this.pin = builder.pin;
        this.balance = builder.balance;
        this.creditLimit = builder.creditLimit;
        this.currencyCode = builder.currencyCode;
        this.status = builder.status;
        this.type = builder.type;
        this.bankName = builder.bankName;
        this.issueDate = builder.issueDate;
        this.rewardPoints = builder.rewardPoints;
        this.interestRate = builder.interestRate;
        this.contactless = builder.contactless;
        this.isBlocked = builder.isBlocked;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long userId;
        private String cardNumber;
        private String cardholderName;
        private LocalDate expirationDate;
        private String cvv;
        private String pin;
        private BigDecimal balance;
        private BigDecimal creditLimit;
        private String currencyCode;
        private CreditCardStatus status = CreditCardStatus.EXPIRED; // Дефолтное значение из вашей модели
        private CreditCardType type = CreditCardType.VISA;          // Дефолтное значение из вашей модели
        private String bankName;
        private LocalDate issueDate;
        private Long rewardPoints;
        private BigDecimal interestRate;
        private Boolean contactless;
        private Boolean isBlocked;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder cardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public Builder cardholderName(String cardholderName) {
            this.cardholderName = cardholderName;
            return this;
        }

        public Builder expirationDate(LocalDate expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder cvv(String cvv) {
            this.cvv = cvv;
            return this;
        }

        public Builder pin(String pin) {
            this.pin = pin;
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

        public Builder issueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
            return this;
        }

        public Builder rewardPoints(Long rewardPoints) {
            this.rewardPoints = rewardPoints;
            return this;
        }

        public Builder interestRate(BigDecimal interestRate) {
            this.interestRate = interestRate;
            return this;
        }

        public Builder contactless(Boolean contactless) {
            this.contactless = contactless;
            return this;
        }

        public Builder isBlocked(Boolean isBlocked) {
            this.isBlocked = isBlocked;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public CreditCard build() {
            return new CreditCard(this);
        }
    }

    // --- Getters and Setters ---

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCardNumber() {
        return cardNumber;
    }
    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardholderName() {
        return cardholderName;
    }
    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getCvv() {
        return cvv;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }

    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }
    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public CreditCardStatus getStatus() {
        return status;
    }
    public void setStatus(CreditCardStatus status) {
        this.status = status;
    }

    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public CreditCardType getType() {
        return type;
    }
    public void setType(CreditCardType type) {
        this.type = type;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public Long getRewardPoints() {
        return rewardPoints;
    }
    public void setRewardPoints(Long rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Boolean getContactless() {
        return contactless;
    }
    public void setContactless(Boolean contactless) {
        this.contactless = contactless;
    }

    public Boolean getIsBlocked() { return isBlocked; }
    public void setIsBlocked(Boolean isBlocked) { this.isBlocked = isBlocked; }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CreditCard that)) return false;
        return getId().equals(that.getId()) && getUserId().equals(that.getUserId()) &&
            getCardNumber().equals(that.getCardNumber()) && getCardholderName().equals(that.getCardholderName()) &&
            getExpirationDate().equals(that.getExpirationDate()) && getCvv().equals(that.getCvv()) &&
            getPin().equals(that.getPin()) && getBalance().equals(that.getBalance()) &&
            getCreditLimit().equals(that.getCreditLimit()) && getCurrencyCode().equals(that.getCurrencyCode()) &&
            getStatus() == that.getStatus() && getBankName().equals(that.getBankName()) &&
            getType() == that.getType() && getIssueDate().equals(that.getIssueDate()) &&
            getRewardPoints().equals(that.getRewardPoints()) && getInterestRate().equals(that.getInterestRate()) &&
            getIsBlocked().equals(that.getIsBlocked()) && getContactless().equals(that.getContactless());
    }

    @Override
    public int hashCode() {
        int result = 17; // стартовое простое число
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (cardNumber != null ? cardNumber.hashCode() : 0);
        result = 31 * result + (cardholderName != null ? cardholderName.hashCode() : 0);
        result = 31 * result + (expirationDate != null ? expirationDate.hashCode() : 0);
        result = 31 * result + (cvv != null ? cvv.hashCode() : 0);
        result = 31 * result + (pin != null ? pin.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (creditLimit != null ? creditLimit.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (bankName != null ? bankName.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (issueDate != null ? issueDate.hashCode() : 0);
        result = 31 * result + (rewardPoints != null ? rewardPoints.hashCode() : 0);
        result = 31 * result + (interestRate != null ? interestRate.hashCode() : 0);
        result = 31 * result + (contactless != null ? contactless.hashCode() : 0);
        result = 31 * result + (isBlocked != null ? isBlocked.hashCode() : 0);

        return result;
    }

    @Override
    public String toString() {
        return "CreditCard {" +
            "id=" + id +
            ", userId='" + userId + '\'' +
            ", cardNumber='" + cardNumber + '\'' +
            ", cardholderName='" + cardholderName + '\'' +
            ", expirationDate=" + expirationDate +
            ", cvv='" + cvv + '\'' +
            ", pin='" + pin + '\'' +
            ", balance=" + balance +
            ", creditLimit=" + creditLimit +
            ", currencyCode='" + currencyCode + '\'' +
            ", status=" + status +
            ", bankName='" + bankName + '\'' +
            ", type=" + type +
            ", issueDate=" + issueDate +
            ", rewardPoints=" + rewardPoints +
            ", interestRate=" + interestRate +
            ", contactless=" + contactless +
            ", isBlocked=" + isBlocked +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
    }
}

