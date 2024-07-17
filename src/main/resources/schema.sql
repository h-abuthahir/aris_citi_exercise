CREATE TABLE stocks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    symbol VARCHAR(255) NOT NULL,
    price DOUBLE NOT NULL
);

INSERT INTO stocks (symbol, price) VALUES ('AAPL', 150.0);
INSERT INTO stocks (symbol, price) VALUES ('GOOGL', 2700.0);
INSERT INTO stocks (symbol, price) VALUES ('AMZN', 3400.0);