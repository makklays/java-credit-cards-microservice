-- V1__create_credit_cards_table.sql
-- Migration #1: create table credit_cards

-- Creating table 'credit_cards' for saving main data of credit_cards
CREATE TABLE IF NOT EXISTS credit_cards
(
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL,

    card_number         VARCHAR(19),
    cardholder_name     VARCHAR(255),
    expiration_date     DATE,
    cvv                 VARCHAR(4),
    pin                 VARCHAR(6),
    balance             DECIMAL(15,2) NOT NULL,
    credit_limit        DECIMAL(15,2) NOT NULL,
    currency_code       VARCHAR(3), -- например, USD, EUR

    status              VARCHAR(10) NOT NULL DEFAULT 'EXPIRED',
    type                VARCHAR(20) NOT NULL DEFAULT 'VISA',

    bank_name           VARCHAR(255),
    issue_date          DATE,
    reward_points       BIGINT,
    interest_rate       DECIMAL(15,2) NOT NULL,
    contactless         BOOLEAN,
    is_blocked          BOOLEAN NOT NULL DEFAULT FALSE,

    created_at          TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at          TIMESTAMP(6) DEFAULT NOW()
);

-- Adding index for fast searching by user_id
CREATE INDEX idx_credit_cards_user_id ON credit_cards(user_id);

-- Adding index for fast searching by card_number
CREATE INDEX idx_credit_cards_card_number ON credit_cards(card_number);

-- Adding index for fast searching by status
CREATE INDEX idx_credit_cards_status ON credit_cards(status);

