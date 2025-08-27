-- USERS
insert into users (email, password, first_name, last_name, role)
values ('admin@example.com', 'adminpass', 'Admin', 'User', 'ADMIN'),
       ('i@u.com', 'passpass', 'Ivan', 'User', 'USER'),
       ('maria@hb.com', 'pass123', 'Maria', 'Kulikova', 'USER');

-- TAGS
insert into tags (name) values ('vegan'), ('spicy'), ('gluten-free');

-- USER likes/dislikes tags
insert into user_likes_tags (user_id, tag_id) values (2, 1), (3, 2);
insert into user_dislikes_tags (user_id, tag_id) values (2, 3);

-- RESTAURANTS
insert into restaurants (user_id, name, description, image, address_url)
values (1, 'Happy Sushi', 'Best sushi in town', 'sushi.jpg', 'https://maps.example.com/1'),
       (1, 'Pasta House', 'Italian pasta and pizza', 'pasta.jpg', 'https://maps.example.com/2');

-- Working hours
insert into restaurant_working_dates_hours (restaurant_id, day_of_week, open_time, close_time)
values (1, 'MONDAY', '10:00', '22:00'),
       (1, 'TUESDAY', '10:00', '22:00'),
       (2, 'MONDAY', '09:00', '21:00');

-- DISHES
insert into dishes (restaurant_id, name, description, image)
values (1, 'California Roll', 'Classic roll with crab and avocado', 'roll.jpg'),
       (1, 'Salmon Nigiri', 'Fresh salmon on rice', 'nigiri.jpg'),
       (2, 'Spaghetti Carbonara', 'Pasta with pancetta and egg sauce', 'carbonara.jpg');

-- DISH OPTIONS
insert into dish_options (dish_id, name, description, price, image, available, prep_time_minute)
values (1, 'Standard', '6 pieces', 8.5, 'roll_standard.jpg', true, 10),
       (1, 'Large', '12 pieces', 15.0, 'roll_large.jpg', true, 15),
       (3, 'Regular', 'Classic carbonara', 12.0, 'carbonara.jpg', true, 20);

-- DISH OPTION TAGS
insert into dish_option_tags (dish_option_id, tag_id) values (1, 1), (3, 2);

-- CART PRODUCTS
insert into cart_products (user_id, dish_id, dish_option_id, quantity)
values (2, 1, 1, 2),
       (3, 3, 3, 1);

-- DISH REVIEWS
insert into dish_review (user_id, user_name, rating, message, dish_option_id, dish_option_name, dish_option_price)
values (2, 'Ivan User', 4.5, 'Very tasty!', 1, 'Standard', 8.5),
       (3, 'Maria Kulikova', 5.0, 'Perfect pasta!', 3, 'Regular', 12.0);

-- JOIN REQUESTS
insert into join_request (email, first_name, last_name, message, status)
values ('new@restaurant.com', 'John', 'Owner', 'I want to join with my restaurant', 'PENDING');

-- ORDERS
insert into orders (user_id, user_email, payment_id, total_amount, status, creation_source)
values (2, 'i@u.com', 'pay_123', 17.0, 'PENDING', 'USER');

-- ORDER PRODUCTS
insert into order_product (orders_id, dish_option_id, dish_name, price, quantity)
values (1, 1, 'California Roll Standard', 8.5, 2);

-- PAYMENTS
insert into payment (session_id, amount, order_id, status, payment_option)
values ('sess_123', 17.0, 1, 'IN_PROGRESS', 'STRIPE');

-- RESTAURANT REVIEWS
insert into restaurant_review (user_id, user_name, rating, message, restaurant_id, restaurant_name)
values (2, 'Ivan User', 5.0, 'Amazing sushi!', 1, 'Happy Sushi'),
       (3, 'Maria Kulikova', 4.0, 'Good pasta, but a bit salty', 2, 'Pasta House');
