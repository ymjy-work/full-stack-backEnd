CREATE DATABASE IF NOT EXISTS backend
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE backend;

CREATE TABLE IF NOT EXISTS users (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    username      VARCHAR(50)  NOT NULL,
    password      VARCHAR(100) NOT NULL,
    avatar_url    VARCHAR(255) DEFAULT NULL,
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    balance_cents BIGINT       NOT NULL DEFAULT 0,
    plan_id       BIGINT       NOT NULL DEFAULT 1,
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS plans (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    name          VARCHAR(50)  NOT NULL,
    description   VARCHAR(255) NOT NULL,
    price_cents   BIGINT       NOT NULL DEFAULT 0,
    monthly_quota BIGINT       NOT NULL DEFAULT 0,
    duration_days INT          NOT NULL DEFAULT 30,
    status        TINYINT      NOT NULL DEFAULT 0,
    level         INT          NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS login_token (
    id           BIGINT   NOT NULL AUTO_INCREMENT,
    user_id      BIGINT   NOT NULL,
    token        CHAR(64) NOT NULL,
    expires_time DATETIME NOT NULL,
    create_time  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_token (token),
    KEY idx_user_id (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS quota_period (
    id           BIGINT   NOT NULL AUTO_INCREMENT,
    user_id      BIGINT   NOT NULL,
    plan_id      BIGINT   NOT NULL,
    quota_limit  BIGINT   NOT NULL,
    quota_used   BIGINT   NOT NULL DEFAULT 0,
    period_start DATETIME NOT NULL,
    period_end   DATETIME NOT NULL,
    create_time  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_user_period (user_id, period_end)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS usage_record (
    id                    BIGINT      NOT NULL AUTO_INCREMENT,
    user_id               BIGINT      NOT NULL,
    period_id             BIGINT      NOT NULL,
    amount                BIGINT      NOT NULL,
    quota_remaining_after BIGINT      NOT NULL,
    reason                VARCHAR(50) NOT NULL,
    ref_id                BIGINT      DEFAULT NULL,
    create_time           DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_user_time (user_id, create_time),
    KEY idx_period (period_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS subscription_record (
    id            BIGINT   NOT NULL AUTO_INCREMENT,
    user_id       BIGINT   NOT NULL,
    plan_id       BIGINT   NOT NULL,
    price_cents   BIGINT   NOT NULL,
    balance_after BIGINT   NOT NULL,
    period_id     BIGINT   NOT NULL,
    create_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_user (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

INSERT INTO plans (id, name, description, price_cents, monthly_quota, duration_days, status, level) VALUES
    (1, 'free',  '免费用户套餐\n每月100积分',   0,     100,  30, 1, 0),
    (2, 'plus',  'plus用户套餐\n每月1000积分', 2000,  2000, 30, 1, 1),
    (3, 'pro',   'pro用户套餐\n每月4000积分',  6000,  4000, 30, 1, 2),
    (4, 'ultra', 'ultra用户套餐\n每月8000积分', 10000, 8000, 30, 1, 3);
