-- V7__create_credit_cards_idempotent_table.sql
-- Migration #7: create table credit_cards_idempotent

-- Creating table 'credit_cards_idempotent' for saving main data of credit_cards_idempotent
CREATE TABLE IF NOT EXISTS credit_cards_idempotent
(
    idempotency_key  VARCHAR(255) PRIMARY KEY, -- Unique client UUID token
    response_body    TEXT,                     -- Response cache (to return the same JSON in case of a duplicate, if needed)
    status           VARCHAR(20) NOT NULL,     -- Operation status: 'PROCESSING' or 'COMPLETED'
    created_at       TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6)
);

-- Index for scheduled cleanup of expired keys (if needed)
CREATE INDEX IF NOT EXISTS idx_credit_cards_idempotent_created_at ON credit_cards_idempotent(created_at);

