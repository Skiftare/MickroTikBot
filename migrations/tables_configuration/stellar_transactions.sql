CREATE TABLE IF NOT EXISTS stellar_transactions (
    transaction_hash VARCHAR(255) UNIQUE,
    memo VARCHAR(255) REFERENCES users(key_for_recognizing),
    amount  DECIMAL(13, 7) DEFAULT 0.0000000 NOT NULL,
    source_account VARCHAR(255)
    timestamp TIMESTAMP
);
