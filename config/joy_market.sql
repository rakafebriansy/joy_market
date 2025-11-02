CREATE DATABASE joymarket CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE joymarket;

-- users: all system users (auth)
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(128) NOT NULL,
  role ENUM('CUSTOMER','ADMIN','COURIER') NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- customers: profile details for customers
CREATE TABLE customers (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL UNIQUE,
  full_name VARCHAR(100) NOT NULL,
  phone VARCHAR(20) NOT NULL,
  address TEXT NOT NULL,
  gender ENUM('MALE','FEMALE','OTHER'),
  balance BIGINT DEFAULT 0, -- store in smallest currency unit (e.g. cents) or use integer rupiah
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- categories
CREATE TABLE categories (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE
);

-- products
CREATE TABLE products (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  category_id INT,
  price BIGINT NOT NULL, -- in smallest unit, e.g. cents or use rupiah integer
  stock INT NOT NULL DEFAULT 0,
  is_fresh BOOLEAN DEFAULT TRUE,
  description TEXT,
  FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- promos
CREATE TABLE promos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(50) NOT NULL UNIQUE,
  percentage INT DEFAULT 0, -- e.g. 10 for 10%
  flat_amount BIGINT DEFAULT 0, -- alternative
  valid_from DATE,
  valid_to DATE,
  active BOOLEAN DEFAULT TRUE
);

-- carts
CREATE TABLE carts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL UNIQUE,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- cart_items
CREATE TABLE cart_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  cart_id INT NOT NULL,
  product_id INT NOT NULL,
  count INT NOT NULL,
  FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- orders
CREATE TABLE orders (
  id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  promo_id INT NULL,
  total_price BIGINT NOT NULL, -- final total after promo
  status ENUM('PENDING','PAID','ASSIGNED','IN_PROGRESS','DELIVERED','CANCELLED') DEFAULT 'PENDING',
  courier_id INT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customers(id),
  FOREIGN KEY (promo_id) REFERENCES promos(id),
  FOREIGN KEY (courier_id) REFERENCES users(id)
);

-- order_items
CREATE TABLE order_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL,
  product_id INT NOT NULL,
  count INT NOT NULL,
  price BIGINT NOT NULL, -- unit price at purchase time
  FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
  FOREIGN KEY (product_id) REFERENCES products(id)
);

-- topups
CREATE TABLE topups (
  id INT AUTO_INCREMENT PRIMARY KEY,
  customer_id INT NOT NULL,
  amount BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (customer_id) REFERENCES customers(id)
);
