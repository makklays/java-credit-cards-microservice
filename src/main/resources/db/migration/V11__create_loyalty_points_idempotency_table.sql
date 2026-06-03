-- V11__create_loyalty_points_idempotency_table.sql
-- Migration #11: create table loyalty_points_idempotency

-- Creating table 'loyalty_points_idempotency' for saving main data of loyalty_points_idempotency
CREATE TABLE IF NOT EXISTS loyalty_points_idempotency (
    idempotency_key VARCHAR(255) PRIMARY KEY, -- Unique client UUID token from HTTP header
    response_body TEXT,                       -- Response cache (to return the same JSON in case of a duplicate)
    status VARCHAR(20) NOT NULL,              -- Operation status: 'PROCESSING' or 'COMPLETED'
    created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

-- Index for scheduled cleanup of expired keys (critical for performance as the table grows)
CREATE INDEX IF NOT EXISTS idx_loyalty_points_idempotency_created_at ON loyalty_points_idempotency(created_at);

