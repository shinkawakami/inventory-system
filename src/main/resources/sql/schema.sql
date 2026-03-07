CREATE TABLE product (
    product_id SERIAL PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    price INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO product (product_name, price)
VALUES
('ノートPC', 120000),
('マウス', 3000),
('キーボード', 8000);

CREATE TABLE warehouse (
    warehouse_id SERIAL PRIMARY KEY,
    warehouse_name VARCHAR(100) NOT NULL,
    location VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO warehouse (warehouse_name, location)
VALUES
('東京倉庫', '東京都江東区'),
('大阪倉庫', '大阪府大阪市'),
('福岡倉庫', '福岡県福岡市');

CREATE TABLE stock (
    stock_id SERIAL PRIMARY KEY,
    product_id INTEGER NOT NULL,
    warehouse_id INTEGER NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_product
        FOREIGN KEY (product_id) REFERENCES product(product_id),
    CONSTRAINT fk_stock_warehouse
        FOREIGN KEY (warehouse_id) REFERENCES warehouse(warehouse_id),
    CONSTRAINT uq_stock_product_warehouse
        UNIQUE (product_id, warehouse_id)
);

INSERT INTO stock (product_id, warehouse_id, quantity)
VALUES
(1, 1, 10),
(1, 2, 5),
(2, 1, 30),
(3, 3, 15);

CREATE TABLE stock_history (
    history_id SERIAL PRIMARY KEY,
    stock_id INTEGER NOT NULL,
    history_type VARCHAR(20) NOT NULL,
    quantity INTEGER NOT NULL,
    note VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_stock_history_stock
        FOREIGN KEY (stock_id) REFERENCES stock(stock_id)
);