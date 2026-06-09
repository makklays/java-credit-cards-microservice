-- V2__create_currencies_table.sql
-- Migration #2: create table currencies

-- Creating table 'currencies' for saving main data of currencies from National Bank Ukraine API
-- Эта таблица обновляется из Kafka (rate) из микросервиса java-currencies-microservice
CREATE TABLE IF NOT EXISTS currencies
(
    -- здесь id не нужен, так как мы будем использовать cc в качестве уникального идентификатора (сс уникален)
    cc              VARCHAR(255) NOT NULL,      -- currency code (например, USD, EUR)
    r030            INT NOT NULL,
    title           VARCHAR(255) NOT NULL,
    rate            DOUBLE PRECISION NOT NULL,  -- вместо FLOAT
    exchangedate    VARCHAR(255) NOT NULL,
    created_at      TIMESTAMP(6) DEFAULT NOW()  -- datetime(6) -> TIMESTAMP(6) с микросекундами
);

-- Creation of index for fast search by currency code
CREATE INDEX idx_cc ON currencies(cc);

-- Creation of index for fast search by exchange date
CREATE INDEX idx_exchangedate ON currencies(exchangedate);

