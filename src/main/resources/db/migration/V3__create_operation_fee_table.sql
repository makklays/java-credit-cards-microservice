-- V3__create_operation_fees_table.sql
-- Migration #3: create table operation_fees

-- Creating table 'operation_fees' for saving main data of commissions
CREATE TABLE IF NOT EXISTS operation_fees (
    id                  BIGSERIAL PRIMARY KEY,
    operation_type      VARCHAR(50) NOT NULL,  -- e.g., 'withdrawal', 'deposit', 'transfer'
    description         TEXT,  -- текстовое описание, комментарии к комиссии
    fee_percentage      NUMERIC(10,4),
    fee_fixed           NUMERIC(15,2),
    currency_code       VARCHAR(3) DEFAULT 'USD',  -- для разных валют разные комиссии
    min_fee             NUMERIC(15,2),  -- минимальная и максимальная комиссия, чтобы ограничивать процент
    max_fee             NUMERIC(15,2),
    active              BOOLEAN DEFAULT true,  -- флаг активности комиссии, чтобы можно было отключить
    valid_from          TIMESTAMP(6) DEFAULT NULL,  -- если комиссия действует только в определённый период
    valid_to            TIMESTAMP(6) DEFAULT NULL,
    loyalty_percentage  NUMERIC(10,4) DEFAULT 0,  -- процент лояльности для постоянных клиентов
    loyalty_fixed       NUMERIC(15,2) DEFAULT 0,  -- фиксированная часть лояльности
    created_at          TIMESTAMP(6) DEFAULT NOW(),
    updated_at          TIMESTAMP(6) DEFAULT NOW()
);

-- Adding indexes for faster queries on operation_type
CREATE INDEX idx_operation_type ON operation_fees (operation_type);

