CREATE TABLE IF NOT EXISTS users (
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    tg_user_id BIGINT PRIMARY KEY NOT NULL,
    phone VARCHAR(20),
    name VARCHAR(255) NOT NULL,
    vpn_profile TEXT,
    expired_at DATE
)
