-- V10__create_loyalty_points_cqrs_read_table.sql
-- Migration #10: create table loyalty_points_cqrs_read

-- Creating table 'loyalty_points_cqrs_read' for reading main data of loyalty_points (CQRS pattern)
CREATE TABLE IF NOT EXISTS loyalty_points_cqrs_read (
    credit_card_id      BIGINT PRIMARY KEY,               -- card_id как уникальный ключ для быстрого поиска
    points_balance      BIGINT NOT NULL DEFAULT 0,
    expiration_date     TIMESTAMP(6) NULL,
    updated_at          TIMESTAMP(6) NOT NULL DEFAULT NOW()
);

-- Индекс для мгновенной витрины личного кабинета
CREATE INDEX IF NOT EXISTS idx_lp_cqrs_read_card ON loyalty_points_cqrs_read(credit_card_id);

