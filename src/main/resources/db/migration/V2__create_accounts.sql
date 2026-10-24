CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    client_id UUID NOT NULL REFERENCES clients(id),
    account_number VARCHAR(20) UNIQUE NOT NULL,
    balance NUMERIC(19,2) NOT NULL CHECK ( balance >= 0 ),
    currency VARCHAR(3) NOT NULL,
    status VARCHAR NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);