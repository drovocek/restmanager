DROP TABLE user_roles IF EXISTS;
DROP TABLE users IF EXISTS;

-- DROP TABLE dish IF EXISTS;
-- DROP TABLE menu_item IF EXISTS;
-- DROP TABLE menu IF EXISTS;
-- DROP TABLE restaurant IF EXISTS;
-- DROP TABLE likes IF EXISTS;

-- DROP SEQUENCE global_seq1 IF EXISTS;
-- DROP SEQUENCE global_seq2 IF EXISTS;
-- DROP SEQUENCE global_seq3 IF EXISTS;
-- DROP SEQUENCE global_seq4 IF EXISTS;
-- DROP SEQUENCE global_seq5 IF EXISTS;
-- DROP SEQUENCE global_seq6 IF EXISTS;

-- CREATE SEQUENCE GLOBAL_SEQ1 AS INTEGER START WITH 0;
-- CREATE SEQUENCE GLOBAL_SEQ2 AS INTEGER START WITH 0;
-- CREATE SEQUENCE GLOBAL_SEQ3 AS INTEGER START WITH 0;
-- CREATE SEQUENCE GLOBAL_SEQ4 AS INTEGER START WITH 0;
-- CREATE SEQUENCE GLOBAL_SEQ5 AS INTEGER START WITH 0;
-- CREATE SEQUENCE GLOBAL_SEQ6 AS INTEGER START WITH 0;

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
    id      INT IDENTITY NOT NULL,
    name    VARCHAR(255) NOT NULL,
    phone   VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX restaurant_unique_name_idx ON restaurant (name);

CREATE TABLE likes
(
    id            INT IDENTITY       NOT NULL,
    user_id       INTEGER            NOT NULL,
    restaurant_id INTEGER            NOT NULL,
    like_date     DATE DEFAULT now() NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX likes_unique_idx ON likes (user_id, like_date);

--
-- CREATE TABLE dish
-- (
--     id      INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ6 PRIMARY KEY,
--     name    VARCHAR(255)         NOT NULL,
--     price   VARCHAR(255)         NOT NULL,
--     enabled BOOLEAN DEFAULT TRUE NOT NULL
-- );
--
-- CREATE TABLE menu
-- (
--     id            INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ4 PRIMARY KEY,
--     name          VARCHAR(255)       NOT NULL,
--     restaurant_id INTEGER            NOT NULL,
--     menu_date     DATE DEFAULT now() NOT NULL,
--     FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
-- );
--
-- CREATE TABLE menu_item
-- (
--     id      INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ3 PRIMARY KEY,
--     menu_id INTEGER NOT NULL,
--     dish_id INTEGER NOT NULL,
--     FOREIGN KEY (menu_id) REFERENCES menu (id) ON DELETE CASCADE,
--     FOREIGN KEY (dish_id) REFERENCES dish (id) ON DELETE CASCADE
-- );
--
--
--

--
--
--
--
--
