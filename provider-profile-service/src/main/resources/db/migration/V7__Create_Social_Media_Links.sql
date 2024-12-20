CREATE TABLE social_media_links (
            business_id BIGINT NOT NULL,
            link TEXT NOT NULL,
            PRIMARY KEY (business_id, link),
            CONSTRAINT fk_business FOREIGN KEY (business_id) REFERENCES about (business_id) ON DELETE CASCADE
);