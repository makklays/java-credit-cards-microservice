package com.techmatrix18.model;

import com.techmatrix18.enums.CreditCardStatus;
import com.techmatrix18.enums.CreditCardType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
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
    private LocalDateTime expirationDate;

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
    private Instant issueDate;

    @Column("reward_points")
    private Integer rewardPoints;

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

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(LocalDateTime expirationDate) {
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

    public Instant getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(Instant issueDate) {
        this.issueDate = issueDate;
    }

    public Integer getRewardPoints() {
        return rewardPoints;
    }
    public void setRewardPoints(Integer rewardPoints) {
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

