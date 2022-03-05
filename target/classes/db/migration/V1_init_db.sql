CREATE TABLE IF NOT EXISTS messaging.users (
    id BIGSERIAL GENERATED ALWAYS AS IDENTITY,
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(100) NOT NULL UNIQUE,
    deleted bit(1) DEFAULT 0,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS messaging.messages (
    id BIGSERIAL GENERATED ALWAYS AS IDENTITY,
    created_at TIMESTAMP(3) DEFAULT CURRENT_TIMESTAMP,
    from_user BIGINT NOT NULL,
    to_user BIGINT NOT NULL,
    message TEXT NOT NULL,
    deleted bit(1) DEFAULT 0,
    PRIMARY KEY(id),
    CONSTRAINT fk_from_user FOREIGN KEY (from_user) REFERENCES messaging.users(id),
    CONSTRAINT fk_to_user FOREIGN KEY (to_user) REFERENCES messaging.users(id)
);