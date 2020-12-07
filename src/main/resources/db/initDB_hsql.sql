DROP TABLE menu_item IF EXISTS;
DROP TABLE menu IF EXISTS;
DROP TABLE vote IF EXISTS;
DROP TABLE user_roles IF EXISTS;
DROP TABLE users IF EXISTS;
DROP TABLE restaurant IF EXISTS;

CREATE TABLE users
(
    id         INT IDENTITY          NOT NULL,
    name       VARCHAR(255)          NOT NULL,
    email      VARCHAR(255)          NOT NULL,
    password   VARCHAR(255)          NOT NULL,
    registered DATE    DEFAULT now() NOT NULL,
    enabled    BOOLEAN DEFAULT TRUE  NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_roles
(
    user_id INTEGER NOT NULL,
    role    VARCHAR(255),
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurant
(
    id      INT IDENTITY          NOT NULL,
    name    VARCHAR(255)          NOT NULL,
    phone   VARCHAR(255)          NOT NULL,
    address VARCHAR(255)          NOT NULL,
    enabled BOOLEAN DEFAULT FALSE NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX restaurant_unique_name_idx ON restaurant (name);

CREATE TABLE vote
(
    id            INT IDENTITY       NOT NULL,
    user_id       INTEGER            NOT NULL,
    restaurant_id INTEGER            NOT NULL,
    vote_date     DATE DEFAULT now() NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id)  ON DELETE CASCADE
);
CREATE UNIQUE INDEX votes_unique_idx ON vote (user_id, vote_date);

CREATE TABLE menu
(
    id            INT IDENTITY          NOT NULL,
    name          VARCHAR(255)          NOT NULL,
    restaurant_id INTEGER               NOT NULL,
    menu_date     DATE    DEFAULT now() NOT NULL,
    enabled       BOOLEAN DEFAULT FALSE NOT NULL,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);

CREATE TABLE menu_item
(
    id      INT IDENTITY          NOT NULL,
    name    VARCHAR(255)          NOT NULL,
    menu_id INTEGER               NOT NULL,
    price   INTEGER               NOT NULL,
    FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE
);