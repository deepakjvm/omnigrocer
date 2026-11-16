-- OmniGrocer PostgreSQL Schema Migration

-- 1. Category table
CREATE TABLE IF NOT EXISTS category (
    category_id SERIAL PRIMARY KEY,
    name        VARCHAR(255)
);

-- 2. Customer table
CREATE TABLE IF NOT EXISTS customer (
    id       SERIAL PRIMARY KEY,
    username VARCHAR(255) UNIQUE,
    email    VARCHAR(255),
    password VARCHAR(255),
    role     VARCHAR(255),
    address  VARCHAR(255)
);

-- 3. Product table
CREATE TABLE IF NOT EXISTS product (
    product_id  SERIAL PRIMARY KEY,
    name        VARCHAR(255),
    image       VARCHAR(255),
    price       INTEGER,
    quantity    INTEGER,
    weight      INTEGER,
    description VARCHAR(255),
    category_id INTEGER REFERENCES category (category_id),
    customer_id INTEGER REFERENCES customer (id)
);

-- 4. Cart table
CREATE TABLE IF NOT EXISTS cart (
    id          SERIAL PRIMARY KEY,
    customer_id INTEGER REFERENCES customer (id)
);

-- 5. Cart Product (Join table for cart items)
CREATE TABLE IF NOT EXISTS cart_product (
    cart_id    INTEGER NOT NULL REFERENCES cart (id) ON DELETE CASCADE,
    product_id INTEGER NOT NULL REFERENCES product (product_id) ON DELETE CASCADE,
    PRIMARY KEY (cart_id, product_id)
);

-- Indexes for foreign key lookups
CREATE INDEX IF NOT EXISTS idx_product_category ON product (category_id);
CREATE INDEX IF NOT EXISTS idx_product_customer ON product (customer_id);
CREATE INDEX IF NOT EXISTS idx_cart_customer ON cart (customer_id);
