CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        user_id UUID NOT NULL,
                        total_amount DECIMAL(10, 2) NOT NULL,
                        status VARCHAR(50) NOT NULL, -- PENDING, COMPLETED, FAILED
                        created_at TIMESTAMP NOT NULL
);

CREATE TABLE order_items (
                             id UUID PRIMARY KEY,
                             order_id UUID NOT NULL REFERENCES orders(id),
                             sku VARCHAR(100) NOT NULL,
                             product_name VARCHAR(255) NOT NULL,
                             quantity INT NOT NULL,
                             unit_price DECIMAL(10, 2) NOT NULL,
                             sub_total DECIMAL(10, 2) NOT NULL
);