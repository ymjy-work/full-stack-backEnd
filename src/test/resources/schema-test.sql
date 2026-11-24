DROP TABLE IF EXISTS subscription_record;
DROP TABLE IF EXISTS usage_record;
DROP TABLE IF EXISTS quota_period;
DROP TABLE IF EXISTS login_token;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS plans;

CREATE TABLE users (
    id           INTEGER PRIMARY KEY,
    username     TEXT NOT NULL,
    password     TEXT NOT NULL,
    avatar_url   TEXT,
    create_time  TEXT NOT NULL DEFAULT (datetime('now','localtime')),
    update_time  TEXT NOT NULL DEFAULT (datetime('now','localtime')),
    balance_cents INTEGER NOT NULL DEFAULT 0,
    plan_id      INTEGER NOT NULL DEFAULT 1,
    UNIQUE (username)
);

CREATE TABLE plans (
    id            INTEGER PRIMARY KEY,
    name          TEXT NOT NULL,
    description   TEXT NOT NULL,
    price_cents   INTEGER NOT NULL DEFAULT 0,
    monthly_quota INTEGER NOT NULL DEFAULT 0,
    duration_days INTEGER NOT NULL DEFAULT 30,
    status        INTEGER NOT NULL DEFAULT 0,
    UNIQUE (name)
);

CREATE TABLE login_token (
    id           INTEGER PRIMARY KEY,
    user_id      INTEGER NOT NULL,
    token        TEXT NOT NULL,
    expires_time TEXT NOT NULL,
    create_time  TEXT NOT NULL DEFAULT (datetime('now','localtime')),
    UNIQUE (token)
);

CREATE TABLE quota_period (
    id           INTEGER PRIMARY KEY,
    user_id      INTEGER NOT NULL,
    plan_id      INTEGER NOT NULL,
    quota_limit  INTEGER NOT NULL,
    quota_used   INTEGER NOT NULL DEFAULT 0,
    period_start TEXT NOT NULL,
    period_end   TEXT NOT NULL,
    create_time  TEXT NOT NULL DEFAULT (datetime('now','localtime')),
    UNIQUE (user_id, period_start)
);

CREATE TABLE usage_record (
    id                    INTEGER PRIMARY KEY,
    user_id               INTEGER NOT NULL,
    period_id             INTEGER NOT NULL,
    amount                INTEGER NOT NULL,
    quota_remaining_after INTEGER NOT NULL,
    reason                TEXT NOT NULL,
    ref_id                TEXT,
    create_time           TEXT NOT NULL DEFAULT (datetime('now','localtime'))
);

CREATE TABLE subscription_record (
    id            INTEGER PRIMARY KEY,
    user_id       INTEGER NOT NULL,
    plan_id       INTEGER NOT NULL,
    price_cents   INTEGER NOT NULL,
    balance_after INTEGER NOT NULL,
    period_id     INTEGER NOT NULL,
    create_time   TEXT NOT NULL DEFAULT (datetime('now','localtime'))
);

INSERT INTO plans (id, name, description, price_cents, monthly_quota, duration_days, status) VALUES
    (1, 'free',  'free',  0,     100,  30, 1),
    (2, 'plus',  'plus',  2000,  2000, 30, 1),
    (3, 'pro',   'pro',   6000,  4000, 30, 1),
    (4, 'ultra', 'ultra', 10000, 8000, 30, 1);

INSERT INTO users (id, username, password, plan_id, balance_cents)
VALUES (1, 'admin', '$2a$10$abcdefghijklmnopqrstuv', 1, 2000);
