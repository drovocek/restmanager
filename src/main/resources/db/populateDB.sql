DELETE
FROM user_roles;
DELETE
FROM users;
-- DELETE
-- FROM restaurant;
-- DELETE
-- FROM menu;
-- DELETE
-- FROM menu_item;
-- DELETE
-- FROM dish;
-- DELETE
-- FROM likes;

-- ALTER SEQUENCE global_seq1 RESTART WITH 0;
-- ALTER SEQUENCE global_seq2 RESTART WITH 0;
-- ALTER SEQUENCE global_seq3 RESTART WITH 0;
-- ALTER SEQUENCE global_seq4 RESTART WITH 0;
-- ALTER SEQUENCE global_seq5 RESTART WITH 0;
-- ALTER SEQUENCE global_seq6 RESTART WITH 0;

INSERT INTO users (id, name, email, password)
VALUES (0, 'User', 'user@yandex.ru', 'password'),
       (1, 'Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 0),
       ('ADMIN', 1),
       ('USER', 1);

-- INSERT INTO restaurant (name, address, phone)
-- VALUES ('rest1', 'address1', '+7 (911) 111-11-11'),
--        ('rest2', 'address2', '+7 (922) 222-22-22'),
--        ('rest3', 'address3', '+7 (933) 333-33-33'),
--        ('rest4', 'address4', '+7 (944) 444-44-44'),
--        ('rest5', 'address5', '+7 (955) 555-55-55');
--
-- INSERT INTO likes (user_id, restaurant_id, like_date)
-- VALUES (0, 0, '2020-01-27'),
--        (0, 0, '2020-01-28'),
--        (0, 1, '2020-01-29'),
--        (0, 2, '2020-01-30'),
--        (0, 0, '2020-02-27'),
--        (0, 3, '2020-03-28'),
--        (0, 3, '2020-03-29'),
--        (0, 4, '2020-03-30');
--
-- INSERT INTO dish (name, price, enabled)
-- VALUES ('gooDish1',111,'true'),
--        ('gooDish1',222,'true'),
--        ('gooDish1',333,'true'),
--        ('gooDish1',444,'true'),
--        ('gooDish1',555,'true');
--
-- INSERT INTO menu (name, restaurant_id,menu_date)
-- VALUES ('menu1',0,'2020-01-27'),
--        ('menu2',0,'2020-02-28');
--
-- INSERT INTO menu_item (menu_id, dish_id)
-- VALUES (0,0),
--        (0,1),
--        (0,2),
--        (0,3),
--        (0,4);