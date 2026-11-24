-- 建表
create table users(
                      id integer primary key ,
                      username text not null ,
                      password text not null ,
                      plan_id integer not null default 1,
                      avatar_url text,
                      create_time text not null  default (datetime('now','localtime')),
                      update_time text not null default (datetime('now','localtime')),
                      unique (username)
);

create table plan(
                     id integer primary key ,
                     name text not null ,
                     description text not null ,
                     price_cents INTEGER not null default 0,
                     unique (name)
);

CREATE TABLE login_token(
                            id INTEGER PRIMARY KEY ,
                            user_id INTEGER not null ,
                            token text not null ,
                            expires_time text not null ,
                            create_time text not null default (datetime('now','localtime')),
                            UNIQUE(token)
);

-- 初始化数据
INSERT INTO plans (name, description, price_cents)
VALUES ('free','免费用户套餐\n每月100积分','0');
INSERT INTO plans (name, description, price_cents)
VALUES ('plus','plus用户套餐\n每月1000积分','2000');
INSERT INTO plans (name, description, price_cents)
VALUES ('pro','pro用户套餐\n每月4000积分','6000');
INSERT INTO plans (name, description, price_cents)
VALUES ('ultra','ultra用户套餐\n每月8000积分','10000');