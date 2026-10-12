CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    client_id UUID FOREIGN KEY NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    balance NUMERIC(19,2) NOT NULL >= 0
);