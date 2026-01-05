-- V4__create_loyalty_points_table.sql
-- Migration #4: create table loyalty_points

-- Creating table 'loyalty_points' for saving main data of loyalty points
CREATE TABLE IF NOT EXISTS loyalty_points (
    id                  BIGSERIAL PRIMARY KEY,
    credit_card_id      BIGINT NOT NULL,
    points_balance      BIGINT DEFAULT 0,
    last_updated        TIMESTAMP(6) NOT NULL,
    expiration_date     TIMESTAMP(6) NULL
);

