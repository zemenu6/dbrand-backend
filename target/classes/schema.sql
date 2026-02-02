-- Create database
CREATE DATABASE IF NOT EXISTS dbrand_db;

-- Use the database
\c dbrand_db;

-- Create UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'USER' CHECK (role IN ('USER', 'ADMIN')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Shoes table
CREATE TABLE shoes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Shoe sizes table
CREATE TABLE shoe_sizes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    shoe_id UUID NOT NULL REFERENCES shoes(id) ON DELETE CASCADE,
    size INTEGER NOT NULL,
    stock_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(shoe_id, size)
);

-- Orders table
CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PROCESSING' CHECK (status IN ('PROCESSING', 'SHIPPED', 'DELIVERED')),
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    delivery_date TIMESTAMP,
    delivery_days INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Order items table
CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    shoe_id UUID NOT NULL REFERENCES shoes(id),
    size INTEGER NOT NULL,
    quantity INTEGER NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Payments table
CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'PAID')),
    transaction_reference VARCHAR(100),
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_shoes_brand ON shoes(brand);
CREATE INDEX idx_shoes_price ON shoes(price);
CREATE INDEX idx_shoe_sizes_shoe_id ON shoe_sizes(shoe_id);
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_order_items_order_id ON order_items(order_id);
CREATE INDEX idx_payments_order_id ON payments(order_id);

-- Insert sample data
INSERT INTO users (name, email, password_hash, role) VALUES
('Admin User', 'admin@dbrand.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'ADMIN'),
('John Doe', 'john@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'USER');

INSERT INTO shoes (name, brand, description, price, image_url) VALUES
('Air Max 90', 'Nike', 'Classic running shoe with Air cushioning', 120.00, 'https://example.com/airmax90.jpg'),
('Ultra Boost', 'Adidas', 'Responsive running shoe with Boost cushioning', 180.00, 'https://example.com/ultraboost.jpg'),
('Chuck Taylor', 'Converse', 'Classic canvas basketball shoe', 60.00, 'https://example.com/chucktaylor.jpg'),
('Old Skool', 'Vans', 'Classic skate shoe with suede panels', 70.00, 'https://example.com/oldskool.jpg');

-- Insert shoe sizes
INSERT INTO shoe_sizes (shoe_id, size, stock_count) VALUES
((SELECT id FROM shoes WHERE name = 'Air Max 90'), 7, 10),
((SELECT id FROM shoes WHERE name = 'Air Max 90'), 8, 15),
((SELECT id FROM shoes WHERE name = 'Air Max 90'), 9, 12),
((SELECT id FROM shoes WHERE name = 'Air Max 90'), 10, 8),
((SELECT id FROM shoes WHERE name = 'Ultra Boost'), 7, 5),
((SELECT id FROM shoes WHERE name = 'Ultra Boost'), 8, 8),
((SELECT id FROM shoes WHERE name = 'Ultra Boost'), 9, 10),
((SELECT id FROM shoes WHERE name = 'Ultra Boost'), 10, 6),
((SELECT id FROM shoes WHERE name = 'Chuck Taylor'), 7, 20),
((SELECT id FROM shoes WHERE name = 'Chuck Taylor'), 8, 25),
((SELECT id FROM shoes WHERE name = 'Chuck Taylor'), 9, 18),
((SELECT id FROM shoes WHERE name = 'Chuck Taylor'), 10, 15),
((SELECT id FROM shoes WHERE name = 'Old Skool'), 7, 12),
((SELECT id FROM shoes WHERE name = 'Old Skool'), 8, 14),
((SELECT id FROM shoes WHERE name = 'Old Skool'), 9, 10),
((SELECT id FROM shoes WHERE name = 'Old Skool'), 10, 8);

-- Create function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create triggers for updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_shoes_updated_at BEFORE UPDATE ON shoes FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_orders_updated_at BEFORE UPDATE ON orders FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_payments_updated_at BEFORE UPDATE ON payments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
