CREATE TABLE account_entries (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL REFERENCES accounts(id),
    flow_funds NUMERIC(19,2),
    operation_type VARCHAR NOT NULL,
    transfer_id UUID REFERENCES transfers(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
)