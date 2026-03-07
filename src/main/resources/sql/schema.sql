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