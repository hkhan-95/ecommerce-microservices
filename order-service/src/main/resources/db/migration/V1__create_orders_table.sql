CREATE TABLE orders (
                        id UUID PRIMARY KEY,
                        product_id UUID NOT NULL,
                        customer_id UUID NOT NULL,
                        order_notes VARCHAR(500) NOT NULL,
                        total_amount NUMERIC(12, 2) NOT NULL,
                        status VARCHAR(30) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
