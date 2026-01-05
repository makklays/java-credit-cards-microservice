-- V6__create_merchants_table.sql
-- Migration #6: create table merchants

-- Creating table 'merchants' for saving main data of merchants
CREATE TABLE IF NOT EXISTS merchants (
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  TEXT,
    category     VARCHAR(255) NOT NULL DEFAULT 'OTHER',
    location     VARCHAR(255),
    phone        VARCHAR(50),
    email        VARCHAR(255),
    website      VARCHAR(255),
    rating       INT DEFAULT 0,
    latitude     DOUBLE PRECISION,
    longitude    DOUBLE PRECISION,
    created_at   TIMESTAMP(6) DEFAULT NOW(),
    updated_at   TIMESTAMP(6) DEFAULT NOW()
);

-- Creating index on category for faster queries
CREATE INDEX idx_merchants_category ON merchants(category);

-- Creating index on location for faster queries
CREATE INDEX idx_merchants_location ON merchants(location);

