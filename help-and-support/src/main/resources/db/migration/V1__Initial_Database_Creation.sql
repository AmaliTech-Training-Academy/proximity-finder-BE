CREATE TABLE faqgroup (
                          id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE faq (
                     id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                     question VARCHAR(255) NOT NULL,
                     answer VARCHAR(255) NOT NULL,
                     group_id BIGINT NOT NULL,
                     CONSTRAINT FK_FAQ_ON_GROUP FOREIGN KEY (group_id) REFERENCES faqgroup(id)
);



