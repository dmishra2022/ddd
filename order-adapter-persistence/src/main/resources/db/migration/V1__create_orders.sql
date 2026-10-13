-- Order Management bounded context: initial schema.
-- Portable across H2 (dev / tests) and PostgreSQL (prod).

CREATE TABLE orders (
    id              UUID            NOT NULL PRIMARY KEY,
    status          VARCHAR(32)     NOT NULL,
    currency_code   CHAR(3)         NOT NULL,
    total_amount    DECIMAL(19, 4)  NOT NULL,
    created_at      TIMESTAMP       NOT NULL
);

CREATE TABLE order_lines (
    id          UUID            NOT NULL PRIMARY KEY,
    order_id    UUID            NOT NULL,
    line_number INTEGER         NOT NULL,
    product_id  VARCHAR(128)    NOT NULL,
    quantity    INTEGER         NOT NULL,
    unit_price  DECIMAL(19, 4)  NOT NULL,
    CONSTRAINT fk_order_lines_order
        FOREIGN KEY (order_id) REFERENCES orders (id) ON DELETE CASCADE,
    CONSTRAINT uq_order_lines_order_line UNIQUE (order_id, line_number)
);

CREATE INDEX idx_order_lines_order_id ON order_lines (order_id);
