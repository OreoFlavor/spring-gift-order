CREATE TABLE PRODUCTS (
    id CHAR(36) PRIMARY KEY,
    name varchar(255),
    price INT,
    imageUrl varchar(255)
);

CREATE TABLE USERS (
    id CHAR(36) PRIMARY KEY,
    email varchar(255),
    password varchar(255),
    salt varchar(64)
);

CREATE TABLE WISHLISTS (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    userId CHAR(36),
    productId CHAR(36),
    FOREIGN KEY (userId) REFERENCES users(id),
    FOREIGN KEY (productId) REFERENCES products(id)
);