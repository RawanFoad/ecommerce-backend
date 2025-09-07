-- =====================================
-- Table: customer
-- =====================================
CREATE TABLE IF NOT EXISTS customer (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
    );

-- =====================================
-- Table: product
-- =====================================
CREATE TABLE IF NOT EXISTS product (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    sku VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    stock_quantity INT NOT NULL
    );

-- =====================================
-- Table: "order"
-- =====================================
CREATE TABLE IF NOT EXISTS "order" (
    id UUID PRIMARY KEY,
    tenant_id VARCHAR(255) NOT NULL,
    customer_id UUID NOT NULL,
    status VARCHAR(50) NOT NULL,
    total_amount NUMERIC(12,2) NOT NULL,
    CONSTRAINT fk_order_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
    );

-- =====================================
-- Table: order_item
-- =====================================
CREATE TABLE IF NOT EXISTS order_item (
    id UUID PRIMARY KEY,
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity INT NOT NULL,
    CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id) REFERENCES "order"(id),
    CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id) REFERENCES product(id)
    );
