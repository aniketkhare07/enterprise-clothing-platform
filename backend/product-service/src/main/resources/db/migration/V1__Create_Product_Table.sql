CREATE TABLE categories (
                            id UUID PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            description TEXT,
                            parent_id UUID REFERENCES categories(id),
                            CONSTRAINT uq_category_name_parent UNIQUE (name, parent_id) -- Allows "Shirts" under "Men" and "Shirts" under "Women"
);

CREATE TABLE products (
                          id UUID PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          description TEXT,
                          category_id UUID REFERENCES categories(id),
                          base_price DECIMAL(10, 2) NOT NULL,
                          image_url VARCHAR(500),
                          created_at TIMESTAMP DEFAULT NOW(),
                          updated_at TIMESTAMP
);

CREATE TABLE product_variants (
                                  id UUID PRIMARY KEY,
                                  product_id UUID REFERENCES products(id) ON DELETE CASCADE,
                                  sku VARCHAR(50) NOT NULL UNIQUE,
                                  size VARCHAR(10),
                                  color VARCHAR(50),
                                  price_adjustment DECIMAL(10, 2) DEFAULT 0.00
);

-- Indexes for performance
CREATE INDEX idx_products_category ON products(category_id);
CREATE INDEX idx_variants_product ON product_variants(product_id);
CREATE INDEX idx_variants_sku ON product_variants(sku);