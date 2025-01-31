# SQL configs
SET SQL_MODE ='IGNORE_SPACE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

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
                                                                   ('John Doe', '66 Sycamore Drive, Staten Island, New York 10314', 'john@doe.com', '123', 'ROLE_ADMIN', 'john'),
                                                                   ('Anna Doe', '3865 Crowfield Road, Phoenix, Arizona 85003', 'anna@doe.com', '123', 'ROLE_NORMAL', 'anna');

CREATE TABLE IF NOT EXISTS PRODUCT(
product_id  int unique key not null auto_increment primary key,
description varchar(255) null,
image       varchar(255) null,
name        varchar(255) null,
price       int null,
quantity    int null,
weight      int null,
category_id int null
);

# insert default products
INSERT INTO PRODUCT(description, image, name, price, quantity, weight, category_id) VALUES
                                                                                        ('Fresh and juicy', 'https://freepngimg.com/save/9557-apple-fruit-transparent/744x744', 'Apple', 3, 40, 76, 1),
                                                                                        ('Woops! There goes the eggs...', 'https://www.nicepng.com/png/full/813-8132637_poiata-bunicii-cracked-egg.png', 'Cracked Eggs', 1, 90, 43, 9);
