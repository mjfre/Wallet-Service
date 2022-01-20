CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

CREATE TABLE IF NOT EXISTS application_user
(
    username                   TEXT NOT NULL PRIMARY KEY,
    password                   TEXT NOT NULL,
    user_role                  user_role    NOT NULL,
    is_account_non_expired     BOOLEAN DEFAULT true,
    is_account_non_locked      BOOLEAN DEFAULT true,
    is_credentials_non_expired BOOLEAN DEFAULT true,
    is_enabled                 BOOLEAN DEFAULT true
);
