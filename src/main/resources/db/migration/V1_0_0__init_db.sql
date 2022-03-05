CREATE SCHEMA IF NOT EXISTS messaging;

CREATE TABLE IF NOT EXISTS messaging.users (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(100) NOT NULL UNIQUE,
    deleted BIT DEFAULT 0::bit
);

CREATE TABLE IF NOT EXISTS messaging.messages (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    from_user BIGINT NOT NULL,
    to_user BIGINT NOT NULL,
    message TEXT NOT NULL,
    deleted BIT DEFAULT 0::bit,
    CONSTRAINT fk_from_user FOREIGN KEY (from_user) REFERENCES messaging.users(id),
    CONSTRAINT fk_to_user FOREIGN KEY (to_user) REFERENCES messaging.users(id)
);