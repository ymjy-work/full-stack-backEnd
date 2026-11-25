DROP TABLE IF EXISTS subscription_record;
DROP TABLE IF EXISTS usage_record;
DROP TABLE IF EXISTS quota_period;
DROP TABLE IF EXISTS login_token;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS plans;

CREATE TABLE users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50) NOT NULL,
    password      VARCHAR(100) NOT NULL,
    avatar_url    VARCHAR(255),
    create_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    balance_cents BIGINT NOT NULL DEFAULT 0,
    plan_id       BIGINT NOT NULL DEFAULT 1,
    CONSTRAINT uk_users_username UNIQUE (username)
);

CREATE TABLE plans (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    description   VARCHAR(255) NOT NULL,
    price_cents   BIGINT NOT NULL DEFAULT 0,
    monthly_quota BIGINT NOT NULL DEFAULT 0,
    duration_days INT NOT NULL DEFAULT 30,
    status        TINYINT NOT NULL DEFAULT 0,
    level         INT NOT NULL DEFAULT 0,
    CONSTRAINT uk_plans_name UNIQUE (name)
);

CREATE TABLE login_token (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    token        CHAR(64) NOT NULL,
    expires_time DATETIME NOT NULL,
    create_time  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_login_token UNIQUE (token)
);

CREATE TABLE quota_period (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id      BIGINT NOT NULL,
    plan_id      BIGINT NOT NULL,
    quota_limit  BIGINT NOT NULL,
    quota_used   BIGINT NOT NULL DEFAULT 0,
    period_start DATETIME NOT NULL,
    period_end   DATETIME NOT NULL,
    create_time  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE usage_record (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id               BIGINT NOT NULL,
    period_id             BIGINT NOT NULL,
    amount                BIGINT NOT NULL,
    quota_remaining_after BIGINT NOT NULL,
    reason                VARCHAR(50) NOT NULL,
    ref_id                BIGINT,
    create_time           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE subscription_record (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    plan_id       BIGINT NOT NULL,
    price_cents   BIGINT NOT NULL,
    balance_after BIGINT NOT NULL,
    period_id     BIGINT NOT NULL,
    create_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO plans (id, name, description, price_cents, monthly_quota, duration_days, status, level) VALUES
    (1, 'free',  'free',  0,     100,  30, 1, 0),
    (2, 'plus',  'plus',  2000,  2000, 30, 1, 1),
    (3, 'pro',   'pro',   6000,  4000, 30, 1, 2),
    (4, 'ultra', 'ultra', 10000, 8000, 30, 1, 3);

INSERT INTO users (id, username, password, plan_id, balance_cents)
VALUES (1, 'admin', '$2a$10$abcdefghijklmnopqrstuv', 1, 2000);
