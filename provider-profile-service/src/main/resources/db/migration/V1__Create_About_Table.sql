CREATE TABLE about (
                       business_id BIGSERIAL PRIMARY KEY,
                       inception_date DATE NOT NULL,
                       social_media_links TEXT[],
                       number_of_employees INT NOT NULL,
                       business_identity_card VARCHAR(255),
                       business_certificate VARCHAR(255),
                       business_summary TEXT,
                       created_by VARCHAR(255)
);
