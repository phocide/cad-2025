CREATE TABLE CATEGORIES (
    category_id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1024)
);

CREATE TABLE PRODUCTS (
    product_id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(1024),
    category_id INT,
    price DECIMAL(10,2),
    stock_quantity INT,
    image_url VARCHAR(1024),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES CATEGORIES(category_id)
); 