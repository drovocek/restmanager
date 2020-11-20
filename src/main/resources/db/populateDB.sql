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
VALUES (0, 'User', 'user@yandex.ru', 'password'),
       (1, 'Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 0),
       ('ADMIN', 1),
       ('USER', 1);

INSERT INTO restaurant (id, name, address, phone)
VALUES (0, 'rest1', 'address1', '+7 (911) 111-1111'),
       (1, 'rest2', 'address2', '+7 (922) 222-2222'),
       (2, 'rest3', 'address3', '+7 (933) 333-3333'),
       (3, 'rest4', 'address4', '+7 (944) 444-4444'),
       (4, 'rest5', 'address5', '+7 (955) 555-5555');

INSERT INTO vote (user_id, restaurant_id, vote_date)
VALUES (0, 0, '2020-01-27'),
       (0, 0, '2020-01-28'),
       (0, 1, '2020-01-29'),
       (0, 2, '2020-01-30'),
       (0, 0, '2020-02-27'),
       (0, 3, '2020-03-28'),
       (0, 3, '2020-03-29'),
       (0, 4, '2020-03-30');

INSERT INTO menu (id, name, restaurant_id, menu_date, enabled)
VALUES (0, 'menu1', 0, '2020-01-27', false),
       (1, 'menu2', 1, '2020-01-27', true),
       (2, 'menu3', 1, '2020-02-28', false),
       (3, 'menu4', 2, '2020-01-27', false),
       (4, 'menu5', 2, '2020-02-28', true),
       (5, 'menu6', 2, '2020-02-28', false),
       (6, 'menu7', 3, '2020-01-27', true),
       (7, 'menu8', 3, '2020-01-27', false),
       (8, 'menu9', 4, '2020-01-27', false),
       (9, 'menu10', 4, '2020-01-28', true),
       (10, 'menu11', 4, '2020-02-29', false);

-- INSERT INTO dish (name, price, enabled)
-- VALUES ('gooDish1',111,'true'),
--        ('gooDish1',222,'true'),
--        ('gooDish1',333,'true'),
--        ('gooDish1',444,'true'),
--        ('gooDish1',555,'true');
--

--
-- INSERT INTO menu_item (menu_id, dish_id)
-- VALUES (0,0),
--        (0,1),
--        (0,2),
--        (0,3),
--        (0,4);