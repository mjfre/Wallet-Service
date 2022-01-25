ALTER TABLE thor_wallet_record ADD CONSTRAINT unique_thor_address UNIQUE (thor_wallet_address);

ALTER TABLE thor_wallet_record ADD CONSTRAINT unique_terra_address UNIQUE (terra_wallet_address);
