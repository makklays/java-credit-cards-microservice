-- V8__create_credit_cards_cqrs_read_table.sql
-- Migration #8: create table credit_cards_cqrs_read

-- Creating table 'credit_cards_cqrs_read' for reading main data of credit_cards (CQRS pattern)
CREATE TABLE IF NOT EXISTS credit_cards_cqrs_read
(
    card_id             BIGINT PRIMARY KEY,             -- Original card ID
    user_id             BIGINT NOT NULL,                -- For fast user card lookup

    masked_card_number  VARCHAR(19) NOT NULL,           -- Masked number (e.g., **** 1234)
    balance             DECIMAL(15,2) NOT NULL,
    credit_limit        DECIMAL(15,2) NOT NULL,
    currency_code       VARCHAR(3),                     -- for example, USD, EUR

    status              VARCHAR(10) NOT NULL DEFAULT 'EXPIRED',
    type                VARCHAR(20) NOT NULL DEFAULT 'VISA',
    bank_name           VARCHAR(255),

    is_blocked          BOOLEAN NOT NULL DEFAULT FALSE, -- Status (ACTIVE, BLOCKED)

    updated_at          TIMESTAMP(6) DEFAULT NOW()
);

-- Index for instant card list rendering on the main screen
CREATE INDEX IF NOT EXISTS idx_cc_cqrs_read_user_id ON credit_cards_cqrs_read(user_id);

