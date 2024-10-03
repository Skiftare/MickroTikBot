CREATE TABLE IF NOT EXISTS users (
    id      BIGINT GENERATED ALWAYS AS IDENTITY,
    tg_user_id BIGINT PRIMARY KEY NOT NULL,
    phone VARCHAR(20),
    name VARCHAR(255) NOT NULL,
    user_last_visited DATE
    vpn_profile TEXT,
    is_vpn_profile_alive BIT
    expired_at DATE
)