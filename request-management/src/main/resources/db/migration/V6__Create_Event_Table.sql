CREATE TABLE event (
           event_id BIGSERIAL PRIMARY KEY,
           title VARCHAR(255) NOT NULL,
           start_date VARCHAR(50) NOT NULL,
           start_time VARCHAR(50) NOT NULL,
           end_date VARCHAR(50),
           end_time VARCHAR(50),
           description TEXT,
           created_by VARCHAR(100) NOT NULL,
           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
