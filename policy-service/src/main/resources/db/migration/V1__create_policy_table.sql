CREATE TABLE policies (
                          id UUID PRIMARY KEY,
                          policy_number VARCHAR(50) NOT NULL UNIQUE,
                          customer_id UUID NOT NULL,
                          vehicle_vin VARCHAR(17) NOT NULL,
                          coverage_type VARCHAR(30) NOT NULL,
                          coverage_limit NUMERIC(12, 2) NOT NULL,
                          status VARCHAR(20) NOT NULL,
                          effective_date DATE NOT NULL,
                          expiration_date DATE NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);