-- V5__create_card_transactions_table.sql
-- Migration #5: create table card_transactions

-- Creating table 'card_transactions' for saving main data of credit card transactions
CREATE TABLE IF NOT EXISTS card_transactions (
    id              BIGSERIAL PRIMARY KEY,

    credit_card_id  BIGINT NOT NULL,
    merchant_id     BIGINT NOT NULL,

    operation_type  VARCHAR(50) NOT NULL, -- WITHDRAWAL, DEPOSIT, TRANSFER
    amount          NUMERIC(15,2) NOT NULL,
    fee_amount      NUMERIC(15,2),
    loyalty_points  INTEGER DEFAULT 0,
    currency_id     BIGINT,
    currency_code   VARCHAR(3) DEFAULT 'USD',
    status          VARCHAR(20) DEFAULT 'PENDING', -- PENDING, COMPLETED, FAILED
    created_at      TIMESTAMP(6) DEFAULT NOW(),
    updated_at      TIMESTAMP(6) DEFAULT NOW()
);

-- Index to optimize queries filtering by credit_card_id
CREATE INDEX idx_transactions_credit_card_id ON card_transactions(credit_card_id);

-- Index to optimize queries filtering by status
CREATE INDEX idx_transactions_status ON card_transactions(status);

