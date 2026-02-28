CREATE TABLE inventory (
                           id UUID PRIMARY KEY,
                           sku VARCHAR(50) NOT NULL UNIQUE,
                           quantity INTEGER NOT NULL DEFAULT 0,
                           reserved_quantity INTEGER NOT NULL DEFAULT 0,
                           last_updated TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_inventory_sku ON inventory(sku);