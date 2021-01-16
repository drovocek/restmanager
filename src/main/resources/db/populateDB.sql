DELETE
FROM menu_item;
DELETE
FROM menu;
DELETE
FROM vote;
DELETE
FROM user_roles;
DELETE
FROM users;
DELETE
FROM restaurant;


INSERT INTO users (id, name, email, password)
VALUES (0, 'User1', 'user1@yandex.ru', '{noop}password1'),
       (1, 'User2', 'user2@yandex.ru', '{noop}password2'),
       (2, 'Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 0),
       ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO restaurant (id, name, address, phone, enabled)
VALUES (0, 'rest1', 'address1', '+7 (911) 111-1111', true),
       (1, 'rest2', 'address2', '+7 (922) 222-2222', false);

INSERT INTO vote (id, user_id, restaurant_id, vote_date)
VALUES (0, 0, 0, '2020-01-27'),
       (1, 0, 0, '2020-01-28'),
       (2, 0, 0, '2020-01-29'),
       (3, 0, 1, '2020-01-30'),
       (4, 0, 1, '2020-01-31'),
       (5, 1, 0, '2020-01-27'),
       (6, 1, 1, '2020-01-28'),
       (7, 1, 1, '2020-01-29');

INSERT INTO menu (id, name, restaurant_id, menu_date, enabled)
VALUES (0, 'menu1', 0, '2020-01-27', true),
       (1, 'menu2', 0, '2020-01-28', false),
       (2, 'menu3', 0, '2020-01-29', true),
       (3, 'menu4', 1, '2020-01-27', true),
       (4, 'menu5', 1, '2020-01-28', false),
       (5, 'menu6', 1, '2020-01-29', true);

INSERT INTO menu_item (id, name, menu_id, price)
VALUES (0, 'menItm1', 0, 100),
       (1, 'menItm2', 0, 101),
       (2, 'menItm3', 0, 102),
       (3, 'menItm4', 1, 103),
       (4, 'menItm5', 1, 104),
       (5, 'menItm6', 1, 105),
       (6, 'menItm7', 2, 106),
       (7, 'menItm8', 2, 107),
       (8, 'menItm9', 2, 108),
       (9, 'menItm10', 3, 109),
       (10, 'menItm11', 3, 1010),
       (11, 'menItm12', 3, 1011),
       (12, 'menItm13', 4, 1012),
       (13, 'menItm14', 4, 1013),
       (14, 'menItm15', 4, 1014),
       (15, 'menItm16', 5, 1015),
       (16, 'menItm17', 5, 1016),
       (17, 'menItm18', 5, 1017);
