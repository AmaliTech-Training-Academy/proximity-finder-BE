CREATE TABLE booking (
             booking_id BIGSERIAL PRIMARY KEY,
             start_date VARCHAR(50) NOT NULL,
             start_time VARCHAR(50) NOT NULL,
             end_date VARCHAR(50) NOT NULL,
             end_time VARCHAR(50) NOT NULL,
             description TEXT,
             created_by VARCHAR(100) NOT NULL,
             assigned_provider VARCHAR(100),
             status VARCHAR(50) NOT NULL,
             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);