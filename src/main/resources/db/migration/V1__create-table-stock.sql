CREATE TABLE stock (
    id BIGINT NOT NULL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    price_in_cents INT NOT NULL,
    quantity INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_DATE
);