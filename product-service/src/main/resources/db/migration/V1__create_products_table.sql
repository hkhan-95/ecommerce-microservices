CREATE TABLE products (
                          id UUID PRIMARY KEY,
                          sku VARCHAR(50) NOT NULL UNIQUE,
                          seller_id UUID NOT NULL,
                          name VARCHAR(255) NOT NULL,
                          category VARCHAR(30) NOT NULL,
                          price NUMERIC(12, 2) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          available_from DATE NOT NULL,
                          available_until DATE NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
