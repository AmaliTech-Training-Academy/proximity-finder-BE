CREATE TABLE quote_image (
             id BIGSERIAL PRIMARY KEY,
             quote_id BIGINT NOT NULL,
             file_path TEXT NOT NULL,
             FOREIGN KEY (quote_id) REFERENCES quotes(quote_id) ON DELETE CASCADE
);
