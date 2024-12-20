CREATE TABLE quote_request (
           request_id BIGSERIAL PRIMARY KEY,
           client_name VARCHAR(100) NOT NULL,
           description TEXT,
           client_email VARCHAR(100),
           request_date VARCHAR(50) NOT NULL,
           assigned_provider VARCHAR(100),
           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
