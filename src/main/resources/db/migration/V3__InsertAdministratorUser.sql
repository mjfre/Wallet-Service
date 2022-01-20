INSERT INTO application_user (
    username,
    password,
    user_role,
    is_account_non_expired,
    is_account_non_locked,
    is_credentials_non_expired,
    is_enabled
)
VALUES (
    'administrator',
    '$2a$10$xguALo0w7DSt6k2njaKNDOo15TgfG6SkZohzTEkdhnz.jjtIcxU/W',
    'ADMIN',
    true,
    true,
    true,
    true
)
ON CONFLICT (username) DO NOTHING;
