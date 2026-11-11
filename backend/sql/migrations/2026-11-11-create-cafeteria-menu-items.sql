USE cput_finder;

CREATE TABLE IF NOT EXISTS Cafeteria_Menu_Item (
    menu_item_id INT NOT NULL AUTO_INCREMENT,
    category VARCHAR(20) NOT NULL,
    item_name VARCHAR(150) NOT NULL,
    price DECIMAL(8,2) NOT NULL,
    display_order INT NOT NULL DEFAULT 0,
    PRIMARY KEY (menu_item_id),
    UNIQUE KEY uq_menu_item_category_name (category, item_name),
    KEY idx_menu_item_category_order (category, display_order)
);

INSERT INTO Cafeteria_Menu_Item (category, item_name, price, display_order)
SELECT seed.category, seed.item_name, seed.price, seed.display_order
FROM (
    SELECT 'Breakfast' AS category, 'Eggs, Bacon & Toast' AS item_name, 40.00 AS price, 1 AS display_order
    UNION ALL SELECT 'Breakfast', 'Eggs & Toast', 30.00, 2
    UNION ALL SELECT 'Breakfast', 'Hotdog & Chips', 35.00, 3
    UNION ALL SELECT 'Breakfast', 'Coffee / Tea', 15.00, 4
    UNION ALL SELECT 'Lunch', 'Pap & Tripe', 60.00, 1
    UNION ALL SELECT 'Lunch', 'Beef Stew & Pap', 45.00, 2
    UNION ALL SELECT 'Lunch', 'Kota', 40.00, 3
    UNION ALL SELECT 'Lunch', 'Burger & Chips', 60.00, 4
    UNION ALL SELECT 'Dinner', 'Lays/Simba Chips', 20.00, 1
    UNION ALL SELECT 'Dinner', 'Biscuits', 15.00, 2
    UNION ALL SELECT 'Dinner', 'Ice Cream', 15.00, 3
    UNION ALL SELECT 'Dinner', 'Soft Drinks', 10.00, 4
) AS seed
WHERE NOT EXISTS (
    SELECT 1
    FROM Cafeteria_Menu_Item existing
    WHERE existing.category = seed.category
      AND existing.item_name = seed.item_name
);
