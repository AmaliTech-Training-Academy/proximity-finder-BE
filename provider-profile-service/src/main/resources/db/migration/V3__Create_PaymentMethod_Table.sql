-- Base table for common fields
CREATE TABLE payment_method (
                                id BIGSERIAL PRIMARY KEY,
                                payment_preference_id BIGINT NOT NULL,
                                account_name VARCHAR(255),
                                account_alias VARCHAR(255),
                                created_by VARCHAR(255),
                                FOREIGN KEY (payment_preference_id) REFERENCES payment_preference(id)
);

-- Bank payment table with specific fields
CREATE TABLE bank_payment (
                              id BIGINT PRIMARY KEY,  -- Same primary key as payment_method (foreign key)
                              bank_name VARCHAR(255),
                              account_number VARCHAR(255),
                              FOREIGN KEY (id) REFERENCES payment_method(id)  -- Foreign key to payment_method
);

-- Mobile money payment table with specific fields
CREATE TABLE mobile_money_payment (
                                      id BIGINT PRIMARY KEY,  -- Same primary key as payment_method (foreign key)
                                      service_provider VARCHAR(255),
                                      phone_number VARCHAR(255),
                                      FOREIGN KEY (id) REFERENCES payment_method(id)  -- Foreign key to payment_method
);

-- PayPal payment table with specific fields
CREATE TABLE paypal_payment (
                                id BIGINT PRIMARY KEY,  -- Same primary key as payment_method (foreign key)
                                first_name VARCHAR(255),
                                last_name VARCHAR(255),
                                email VARCHAR(255),
                                FOREIGN KEY (id) REFERENCES payment_method(id)  -- Foreign key to payment_method
);
