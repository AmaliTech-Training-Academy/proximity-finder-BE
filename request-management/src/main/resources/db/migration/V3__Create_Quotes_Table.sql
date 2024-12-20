CREATE TABLE quotes (
            quote_id BIGSERIAL PRIMARY KEY,
            title VARCHAR(255) NOT NULL,
            description TEXT,
            location VARCHAR(255),
            additional_details TEXT,
            status VARCHAR(50) NOT NULL,
            start_date VARCHAR(50),
            start_time VARCHAR(50),
            end_date VARCHAR(50),
            end_time VARCHAR(50),
            created_by VARCHAR(100) NOT NULL,
            assigned_provider VARCHAR(100),
            request_id BIGINT,
            request_date DATE NOT NULL DEFAULT CURRENT_DATE,
            FOREIGN KEY (request_id) REFERENCES quote_request(request_id) ON DELETE CASCADE,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
