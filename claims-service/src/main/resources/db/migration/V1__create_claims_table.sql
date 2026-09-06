CREATE TABLE claims (
                        id UUID PRIMARY KEY,
                        policy_id UUID NOT NULL,
                        customer_id UUID NOT NULL,
                        claim_type VARCHAR(30) NOT NULL,
                        description VARCHAR(500) NOT NULL,
                        amount_requested NUMERIC(12, 2) NOT NULL,
                        status VARCHAR(30) NOT NULL,
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);