-- V12__create_card_transactions_cqrs_read_table.sql
-- Migration #12: create table card_transactions_cqrs_read

-- Creating table 'card_transactions_cqrs_read' for reading main data of card_transactions (CQRS pattern)
CREATE TABLE IF NOT EXISTS card_transactions_cqrs_read (
    card_transaction_id  BIGINT PRIMARY KEY,             -- Original transaction ID
    credit_card_id  BIGINT NOT NULL,                -- For fast user/card history lookup
    merchant_id     BIGINT NOT NULL,
    operation_type  VARCHAR(50) NOT NULL,           -- WITHDRAWAL, DEPOSIT, TRANSFER
    amount          DECIMAL(15,2) NOT NULL,
    fee_amount      DECIMAL(15,2),
    loyalty_points  INTEGER DEFAULT 0,
    currency_code   VARCHAR(50) DEFAULT 'USD',
    status          VARCHAR(20) DEFAULT 'PENDING',  -- PENDING, COMPLETED, FAILED
    created_at      TIMESTAMP(6) NOT NULL,          -- Kept from original transaction time
    updated_at      TIMESTAMP(6) NOT NULL
);

-- Compound index for fast transaction history loading with pagination on the main screen
-- Filters by card and immediately sorts by creation time in descending order
CREATE INDEX IF NOT EXISTS idx_tx_cqrs_read_card_date ON card_transactions_cqrs_read(credit_card_id, created_at DESC);

