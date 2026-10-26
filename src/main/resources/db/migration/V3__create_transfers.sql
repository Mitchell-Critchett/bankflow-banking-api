CREATE TABLE transfers (
    id UUID PRIMARY KEY,
    source_account_id UUID NOT NULL REFERENCES accounts(id),
    destination_account_id UUID NOT NULL CHECK (destination_account_id != source_account_id)
        REFERENCES accounts(id) ,
    amount NUMERIC(19,2) NOT NULL CHECK (amount >= 50),
    status VARCHAR NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    idempotency_key UUID NOT NULL UNIQUE
)