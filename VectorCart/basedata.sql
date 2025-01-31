# SQL configs
SET SQL_MODE ='IGNORE_SPACE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

DROP DATABASE IF EXISTS vector_cart_database;
CREATE DATABASE IF NOT EXISTS vector_cart_database;
USE vector_cart_database;

CREATE TABLE IF NOT EXISTS CATEGORY(
category_id int unique key not null auto_increment primary key,
name        varchar(255) null
);

# insert default categories
INSERT INTO CATEGORY(name) VALUES ('Electronics & Gadgets'),
                                  ('Fashion & Apparel'),
                                  ('Home & Living'),
                                  ('Health & Beauty'),
                                  ('Books & Stationery'),
                                  ('Automotive & Accessories'),
                                  ('Sports & Outdoors'),
                                  ('Toys & Games'),
                                  ('Food & Groceries'),
                                  ('Luxury & Collectibles'),
                                  ('Other');

CREATE TABLE IF NOT EXISTS USER(
id       int unique key not null auto_increment primary key,
name     varchar(255) null,
address  varchar(255) null,
email    varchar(255) null,
password varchar(255) null,
role     varchar(255) null,
username varchar(255) null,
UNIQUE (username)
);

INSERT INTO USER(name, address, email, password, role, username) VALUES
                                                                   ('John Doe', '66 Sycamore Drive, Staten Island, New York 10314', 'john@doe.com', 'john', 'ROLE_ADMIN', 'john'),
                                                                   ('Jane Doe', '3865 Crowfield Road, Phoenix, Arizona 85003', 'anna@doe.com', 'jane', 'ROLE_NORMAL', 'anna');

CREATE TABLE IF NOT EXISTS PRODUCT(
product_id  int unique key not null auto_increment primary key,
name        varchar(255) null,
brand        varchar(255) null,
description varchar(255) null,
image       varchar(255) null,
price       int null,
quantity    int null,
category_id int null
);

INSERT INTO PRODUCT (name, brand, description, image, price, quantity, category_id) VALUES
    ('iPhone 15 - 128GB', 'Apple', 'iPhone 15 with 128GB storage', 'https://fdn2.gsmarena.com/vv/bigpic/apple-iphone-15-plus-.jpg', 699, 100, 1),
    ('iPhone 15 Plus - 128GB', 'Apple', 'iPhone 15 Plus with 128GB storage', 'https://fdn2.gsmarena.com/vv/bigpic/apple-iphone-15-.jpg', 799, 100, 1),
    ('iPhone 15 Pro - 128GB', 'Apple', 'iPhone 15 Pro with 128GB storage', 'https://fdn2.gsmarena.com/vv/bigpic/apple-iphone-15-pro.jpg', 999, 100, 1),
    ('iPhone 15 Pro Max - 1TB', 'Apple', 'iPhone 15 Pro Max with 1TB storage', 'https://fdn2.gsmarena.com/vv/bigpic/apple-iphone-15-pro-max.jpg', 1599, 100, 1),
    ('iPhone 16 - 128GB', 'Apple', 'iPhone 16 with 128GB storage', 'https://fdn2.gsmarena.com/vv/bigpic/apple-iphone-16.jpg', 799, 100, 1),
    ('iPhone 16 Plus - 128GB', 'Apple', 'iPhone 16 Plus with 128GB storage', 'https://fdn2.gsmarena.com/vv/bigpic/apple-iphone-16-plus.jpg', 899, 100, 1),
    ('iPhone 16 Pro - 128GB', 'Apple', 'iPhone 16 Pro with 128GB storage', 'https://fdn2.gsmarena.com/vv/bigpic/apple-iphone-16-pro.jpg', 999, 100, 1),
    ('iPhone 16 Pro Max - 256GB', 'Apple', 'iPhone 16 Pro Max with 256GB storage', 'https://fdn2.gsmarena.com/vv/bigpic/apple-iphone-16-pro-max.jpg', 1199, 100, 1);