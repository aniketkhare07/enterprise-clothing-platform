CREATE TABLE carts (
                       id UUID PRIMARY KEY,
                       user_id UUID NOT NULL UNIQUE, -- One cart per user
                       created_at TIMESTAMP DEFAULT NOW(),
                       updated_at TIMESTAMP
);

CREATE TABLE cart_items (
                            id UUID PRIMARY KEY,
                            cart_id UUID REFERENCES carts(id) ON DELETE CASCADE,
                            product_id UUID NOT NULL,
                            sku VARCHAR(50) NOT NULL,
                            product_name VARCHAR(255),
                            quantity INTEGER NOT NULL,
                            unit_price DECIMAL(10, 2) NOT NULL, -- Snapshot of price at time of add
                            sub_total DECIMAL(10, 2) NOT NULL   -- quantity * unit_price
);

CREATE INDEX idx_cart_user ON carts(user_id);