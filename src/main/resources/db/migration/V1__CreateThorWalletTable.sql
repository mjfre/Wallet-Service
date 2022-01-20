CREATE TABLE IF NOT EXISTS thor_wallet_record (
    id UUID NOT NULL PRIMARY KEY,
    thor_wallet_address TEXT NOT NULL,
    terra_wallet_address TEXT
);
