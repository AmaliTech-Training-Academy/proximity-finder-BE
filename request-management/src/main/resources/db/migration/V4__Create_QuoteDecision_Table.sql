CREATE TABLE quote_decision_details (
            id BIGSERIAL PRIMARY KEY,
            quote_id BIGINT UNIQUE,
            price DECIMAL(15, 2),
            approval_details TEXT,
            decline_reason TEXT,
            FOREIGN KEY (quote_id) REFERENCES quotes(quote_id) ON DELETE CASCADE,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
