-- USERS
INSERT INTO users (id, username, password, email, role) VALUES
  (1, 'admin', 'adminpass', 'admin@foodhub.com', 'ADMIN'),
  (2, 'john_doe', 'password1', 'john@example.com', 'CUSTOMER'),
  (3, 'jane_smith', 'password2', 'jane@example.com', 'CUSTOMER'),
  (4, 'mike_johnson', 'password3', 'mike@example.com', 'CUSTOMER'),
  (5, 'sara_lee', 'password4', 'sara@example.com', 'CUSTOMER'),
  (6, 'alex_wong', 'password5', 'alex@example.com', 'CUSTOMER'),
  (7, 'emily_clark', 'password6', 'emily@example.com', 'CUSTOMER'),
  (8, 'david_kim', 'password7', 'david@example.com', 'CUSTOMER'),
  (9, 'lisa_brown', 'password8', 'lisa@example.com', 'CUSTOMER'),
  (10, 'tom_white', 'password9', 'tom@example.com', 'CUSTOMER');

-- CATEGORIES
INSERT INTO categories (id, name) VALUES
  (1, 'Burgers'),
  (2, 'Pizza'),
  (3, 'Drinks'),
  (4, 'Desserts');

-- MENU ITEMS
INSERT INTO menu_items (id, name, description, price, quantity, available, category_id) VALUES
  (1, 'Classic Burger', 'Beef patty, lettuce, tomato, cheese', 8.99, 50, TRUE, 1),
  (2, 'Veggie Burger', 'Plant-based patty, lettuce, tomato', 7.99, 40, TRUE, 1),
  (3, 'Pepperoni Pizza', 'Pepperoni, mozzarella, tomato sauce', 12.99, 30, TRUE, 2),
  (4, 'Margherita Pizza', 'Mozzarella, tomato, basil', 11.99, 25, TRUE, 2),
  (5, 'Coke', 'Chilled soft drink', 2.49, 100, TRUE, 3),
  (6, 'Chocolate Cake', 'Rich chocolate dessert', 5.99, 20, TRUE, 4),
  (7, 'Cheesecake', 'Creamy cheesecake', 6.49, 15, TRUE, 4),
  (8, 'Lemonade', 'Fresh lemonade', 2.99, 80, TRUE, 3),
  (9, 'Chicken Burger', 'Grilled chicken, lettuce, mayo', 9.49, 35, TRUE, 1),
  (10, 'BBQ Pizza', 'BBQ sauce, chicken, onions', 13.49, 20, TRUE, 2);

-- ORDERS
INSERT INTO orders (id, status, total, date, user_id) VALUES
  (1, 'Pending', 25.00, '2025-11-23', 2),
  (2, 'Preparing', 18.50, '2025-11-23', 3),
  (3, 'Ready', 32.75, '2025-11-22', 4),
  (4, 'Delivered', 15.00, '2025-11-22', 5),
  (5, 'Pending', 22.00, '2025-11-24', 6),
  (6, 'Preparing', 19.99, '2025-11-24', 7),
  (7, 'Ready', 27.50, '2025-11-24', 8),
  (8, 'Delivered', 14.25, '2025-11-24', 9),
  (9, 'Pending', 29.99, '2025-11-24', 10),
  (10, 'Delivered', 12.99, '2025-11-24', 2),
  (11, 'Pending', 21.00, '2025-11-24', 3),
  (12, 'Preparing', 17.75, '2025-11-24', 4),
  (13, 'Ready', 30.00, '2025-11-24', 5),
  (14, 'Delivered', 16.50, '2025-11-24', 6),
  (15, 'Pending', 24.99, '2025-11-24', 7),
  (16, 'Preparing', 20.00, '2025-11-24', 8),
  (17, 'Ready', 28.99, '2025-11-24', 9),
  (18, 'Delivered', 13.75, '2025-11-24', 10),
  (19, 'Pending', 26.50, '2025-11-24', 2),
  (20, 'Delivered', 11.99, '2025-11-24', 3);