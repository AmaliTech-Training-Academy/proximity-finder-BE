CREATE TABLE call_request (
          request_id BIGSERIAL PRIMARY KEY,
          client_name VARCHAR(100) NOT NULL,
          phone_number VARCHAR(20) NOT NULL,
          client_email VARCHAR(100),
          status VARCHAR(50) NOT NULL,
          assigned_provider VARCHAR(100),
          request_date DATE NOT NULL DEFAULT CURRENT_DATE,
          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
