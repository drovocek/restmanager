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
VALUES (0, 'User1', 'user1@yandex.ru', 'password1'),
       (1, 'User2', 'user2@yandex.ru', 'password2'),
       (2, 'Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 0),
       ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);

INSERT INTO restaurant (id, name, address, phone, enabled)
VALUES (0, 'rest1', 'address1', '+7 (911) 111-1111', true),
       (1, 'rest2', 'address2', '+7 (922) 222-2222', true),
       (2, 'rest3', 'address3', '+7 (933) 333-3333', false),
       (3, 'rest4', 'address4', '+7 (944) 444-4444', true),
       (4, 'rest5', 'address5', '+7 (955) 555-5555', false);

INSERT INTO vote (id, user_id, restaurant_id, vote_date)
VALUES (0, 0, 0, '2020-01-27'),
       (1, 0, 0, '2020-01-28'),
       (2, 0, 1, '2020-01-29'),
       (3, 0, 2, '2020-01-30'),
       (4, 0, 0, '2020-02-27'),
       (5, 0, 3, '2020-03-28'),
       (6, 0, 3, '2020-03-29'),
       (7, 0, 4, '2020-03-30');

INSERT INTO menu (id, name, restaurant_id, menu_date, enabled)
VALUES (0, 'menu1', 0, '2020-01-27', false),
       (1, 'menu2', 1, '2020-01-27', true),
       (2, 'menu3', 1, '2020-01-28', false),
       (3, 'menu4', 2, '2020-01-27', false),
       (4, 'menu5', 2, '2020-01-28', true),
       (5, 'menu6', 2, '2020-01-28', false),
       (6, 'menu7', 3, '2020-01-27', true),
       (7, 'menu8', 3, '2020-01-27', false),
       (8, 'menu9', 4, '2020-01-27', false),
       (9, 'menu10', 4, '2020-01-28', true),
       (10, 'menu11', 4, '2020-01-28', false);

INSERT INTO menu_item (id, name, menu_id, price)
VALUES (0, 'menItm1', 0, 100),
       (1, 'menItm2', 0, 101),
       (2, 'menItm3', 0, 102),
       (3, 'menItm4', 0, 103),
       (4, 'menItm5', 1, 104),
       (5, 'menItm6', 1, 105),
       (6, 'menItm7', 1, 106),
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
       (17, 'menItm18', 5, 1017),
       (18, 'menItm19', 5, 1018),
       (19, 'menItm20', 5, 1019);
