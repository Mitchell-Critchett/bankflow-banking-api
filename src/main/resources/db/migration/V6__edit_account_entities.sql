ALTER TABLE account_entries
ADD COLUMN idempotency_key UUID DEFAULT gen_random_uuid() NOT NULL UNIQUE