CREATE TABLE t_orders(
    id BIGSERIAL NOT NULL,
    order_number VARCHAR(255) DEFAULT NULL,
    skuCode VARCHAR(255),
    price DECIMAL(19, 2),
    quantity INT,
    PRIMARY KEY (id)
);