-- File: data-sample.sql
-- Dữ liệu mẫu tương thích với schema bạn đã cung cấp (database: RestaurantManagement)
-- Chạy trong context: USE RestaurantManagement;
-- Lưu ý: script này dùng HASHBYTES để mã hóa password hiển thị dạng hex.

USE RestaurantManagement;
GO
SET NOCOUNT ON;
GO

BEGIN TRANSACTION;

------------------------------------------------------------
-- Biến lưu ID
------------------------------------------------------------
DECLARE
    @type_meat INT, @type_sea INT, @type_veg INT, @type_spice INT,
    @cat_app INT, @cat_main INT, @cat_dess INT, @cat_drink INT,
    @role_admin INT, @role_manager INT, @role_waiter INT, @role_chef INT, @role_cashier INT,
    @cust_1 INT, @cust_2 INT, @cust_3 INT,
    @table_1 INT, @table_2 INT, @table_3 INT, @table_4 INT,
    @ing_beef INT, @ing_shrimp INT, @ing_lettuce INT, @ing_fishsauce INT, @ing_sugar INT, @ing_milk INT,
    @sup_1 INT, @sup_2 INT,
    @emp_admin INT, @emp_chef INT, @emp_waiter INT, @emp_cashier INT,
    @rec_grilled INT, @rec_beefstew INT, @rec_spring INT, @rec_coffee INT,
    @item_grilled INT, @item_beef INT, @item_spring INT, @item_cake INT, @item_coffee INT,
    @voucher1 INT, @voucher2 INT,
    @res1 INT, @res2 INT,
    @order1 INT, @order2 INT,
    @import1 INT, @import2 INT;

------------------------------------------------------------
-- 1) type
------------------------------------------------------------
INSERT INTO [type] (type_name, description, status) VALUES
(N'Thịt', N'Các loại thịt (bò, gà, heo)', N'Active'),
(N'Hải sản', N'Các loại tôm, cua, cá, mực', N'Active'),
(N'Rau củ', N'Rau xanh và củ quả', N'Active'),
(N'Gia vị', N'Gia vị, nước sốt, dầu ăn', N'Active');

SELECT @type_meat = type_id FROM [type] WHERE type_name = N'Thịt';
SELECT @type_sea  = type_id FROM [type] WHERE type_name = N'Hải sản';
SELECT @type_veg  = type_id FROM [type] WHERE type_name = N'Rau củ';
SELECT @type_spice= type_id FROM [type] WHERE type_name = N'Gia vị';

------------------------------------------------------------
-- 2) category
------------------------------------------------------------
INSERT INTO category (category_name, description, status) VALUES
(N'Món khai vị', N'Món ăn nhẹ trước bữa chính', N'Active'),
(N'Món chính', N'Món chính trong thực đơn', N'Active'),
(N'Tráng miệng', N'Món ngọt / tráng miệng', N'Active'),
(N'Nước uống', N'Cà phê, nước ép, sinh tố', N'Active');

SELECT @cat_app = category_id FROM category WHERE category_name = N'Món khai vị';
SELECT @cat_main = category_id FROM category WHERE category_name = N'Món chính';
SELECT @cat_dess = category_id FROM category WHERE category_name = N'Tráng miệng';
SELECT @cat_drink = category_id FROM category WHERE category_name = N'Nước uống';

------------------------------------------------------------
-- 3) role
------------------------------------------------------------
INSERT INTO role (role_name, description, status) VALUES
(N'Admin', N'Quản trị hệ thống', N'Active'),
(N'Quản lý', N'Quản lý nhà hàng', N'Active'),
(N'Nhân viên phục vụ', N'Phục vụ khách', N'Active'),
(N'Nhân viên bếp', N'Chuẩn bị món ăn', N'Active'),
(N'Thu ngân', N'Xử lý thanh toán', N'Active');

SELECT @role_admin = role_id FROM role WHERE role_name = N'Admin';
SELECT @role_manager = role_id FROM role WHERE role_name = N'Quản lý';
SELECT @role_waiter = role_id FROM role WHERE role_name = N'Nhân viên phục vụ';
SELECT @role_chef = role_id FROM role WHERE role_name = N'Nhân viên bếp';
SELECT @role_cashier = role_id FROM role WHERE role_name = N'Thu ngân';

------------------------------------------------------------
-- 4) customer
-- (có cột customer_account theo DDL gốc; đặt account tương ứng)
------------------------------------------------------------
INSERT INTO customer (customer_account, password, customer_name, gender, dob, phone_number, email, address, status)
VALUES
(N'khach_ava', N'passA', N'Trần Văn A', N'Male', '1990-05-10', '0905123456', N'avan@example.com', N'123 Nguyễn Trãi', N'Active'),
(N'khach_bvb', N'passB', N'Nguyễn Thị B', N'Female', '1995-11-20', '0912345678', N'bv@example.com', N'456 Lê Lợi', N'Active'),
(N'guest', N'guest', N'Khách Vãng Lai', NULL, NULL, NULL, NULL, NULL, N'Active');

SELECT @cust_1 = customer_id FROM customer WHERE customer_account = N'khach_ava';
SELECT @cust_2 = customer_id FROM customer WHERE customer_account = N'khach_bvb';
SELECT @cust_3 = customer_id FROM customer WHERE customer_account = N'guest';

------------------------------------------------------------
-- 5) table (bàn)
------------------------------------------------------------
INSERT INTO [table] (table_number, table_capacity, status) VALUES
('A01', 4, N'Available'),
('A02', 2, N'Available'),
('A03', 6, N'Available'),
('A04', 8, N'Available');

SELECT @table_1 = table_id FROM [table] WHERE table_number = 'A01';
SELECT @table_2 = table_id FROM [table] WHERE table_number = 'A02';
SELECT @table_3 = table_id FROM [table] WHERE table_number = 'A03';
SELECT @table_4 = table_id FROM [table] WHERE table_number = 'A04';

------------------------------------------------------------
-- 6) ingredient
------------------------------------------------------------
INSERT INTO ingredient (ingredient_name, type_id, unit, status) VALUES
(N'Bò tươi', @type_meat, 180000.00, N'Active'),
(N'Tôm sú', @type_sea, 160000.00, N'Active'),
(N'Xà lách', @type_veg, 15000.00, N'Active'),
(N'Nước mắm', @type_spice, 30000.00, N'Active'),
(N'Đường', @type_spice, 10000.00, N'Active'),
(N'Sữa tươi', @type_veg, 25000.00, N'Active');

SELECT @ing_beef = ingredient_id FROM ingredient WHERE ingredient_name = N'Bò tươi';
SELECT @ing_shrimp = ingredient_id FROM ingredient WHERE ingredient_name = N'Tôm sú';
SELECT @ing_lettuce = ingredient_id FROM ingredient WHERE ingredient_name = N'Xà lách';
SELECT @ing_fishsauce = ingredient_id FROM ingredient WHERE ingredient_name = N'Nước mắm';
SELECT @ing_sugar = ingredient_id FROM ingredient WHERE ingredient_name = N'Đường';
SELECT @ing_milk = ingredient_id FROM ingredient WHERE ingredient_name = N'Sữa tươi';

------------------------------------------------------------
-- 7) supplier
------------------------------------------------------------
INSERT INTO supplier (supplier_name, phone_number, email, address, contact_person, status)
VALUES
(N'VietFoods Co.', '0911222333', 'contact@vietfoods.vn', N'12 Supply Rd', N'Hung', N'Active'),
(N'FarmFresh', '0911333444', 'sales@farmfresh.vn', N'88 Farm Lane', N'Lan', N'Active');

SELECT @sup_1 = supplier_id FROM supplier WHERE supplier_name = N'VietFoods Co.';
SELECT @sup_2 = supplier_id FROM supplier WHERE supplier_name = N'FarmFresh';

------------------------------------------------------------
-- 8) employee (mã hóa password bằng SHA2_256 hex)
------------------------------------------------------------
INSERT INTO employee (emp_account, password, emp_name, gender, dob, phone_number, email, address, role_id, status)
VALUES
(N'admin', CONVERT(NVARCHAR(255), HASHBYTES('MD5', N'admin123'), 2), N'Nguyễn Quản Trị', N'Male', '1988-01-01', '0901112222', N'admin@rms.com', N'Hà Nội', @role_admin, N'Active'),
(N'chef1', CONVERT(NVARCHAR(255), HASHBYTES('MD5', N'chef123'), 2), N'Phạm Đầu Bếp', N'Male', '1990-03-15', '0902223333', N'chef@rms.com', N'Đà Nẵng', @role_chef, N'Active'),
(N'waiter1', CONVERT(NVARCHAR(255), HASHBYTES('MD5', N'waiter123'), 2), N'Trần Phục Vụ', N'Female', '1995-07-07', '0903334444', N'waiter@rms.com', N'Hải Phòng', @role_waiter, N'Active'),
(N'cashier1', CONVERT(NVARCHAR(255), HASHBYTES('MD5', N'cashier123'), 2), N'Ngô Thu Ngân', N'Female', '1992-09-15', '0909990000', N'cashier@rms.com', N'Cần Thơ', @role_cashier, N'Active');

SELECT @emp_admin = emp_id FROM employee WHERE emp_account = N'admin';
SELECT @emp_chef  = emp_id FROM employee WHERE emp_account = N'chef1';
SELECT @emp_waiter= emp_id FROM employee WHERE emp_account = N'waiter1';
SELECT @emp_cashier= emp_id FROM employee WHERE emp_account = N'cashier1';

------------------------------------------------------------
-- 9) recipe
------------------------------------------------------------

INSERT INTO recipe (recipe_name, status) VALUES
(N'Grilled Chicken Recipe', N'Active'),
(N'Beef Stew Recipe', N'Active'),
(N'Spring Roll Recipe', N'Active'),
(N'Coffee Recipe', N'Active');

SELECT @rec_grilled = recipe_id FROM recipe WHERE recipe_name = N'Grilled Chicken Recipe';
SELECT @rec_beefstew = recipe_id FROM recipe WHERE recipe_name = N'Beef Stew Recipe';
SELECT @rec_spring = recipe_id FROM recipe WHERE recipe_name = N'Spring Roll Recipe';
SELECT @rec_coffee = recipe_id FROM recipe WHERE recipe_name = N'Coffee Recipe';

------------------------------------------------------------
-- 10) menu_item
-- (theo DDL gốc: category_id, recipe_id, item_name, image_url, price, description)
------------------------------------------------------------
INSERT INTO menu_item (category_id, recipe_id, item_name, image_url, price, description, status)
VALUES
(@cat_main, @rec_grilled, N'Grilled Chicken Plate', N'/images/grilled_chicken.jpg', 120000, N'Đĩa gà nướng thơm ngon', N'Active'),
(@cat_main, @rec_beefstew, N'Beef Stew Bowl', N'/images/beef_stew.jpg', 150000, N'Tô bò hầm đậm đà', N'Active'),
(@cat_app, @rec_spring, N'Spring Roll (Gỏi cuốn)', N'/images/spring_roll.jpg', 45000, N'Gỏi cuốn tôm thịt tươi', N'Active'),
(@cat_dess, @rec_grilled, N'Carrot Cake Slice', N'/images/carrot_cake.jpg', 45000, N'Bánh cà rốt mềm mịn', N'Active'),
(@cat_drink, @rec_coffee, N'Black Coffee', N'/images/coffee.jpg', 30000, N'Cà phê đen truyền thống', N'Active');

SELECT @item_grilled = menu_item_id FROM menu_item WHERE item_name = N'Grilled Chicken Plate';
SELECT @item_beef = menu_item_id FROM menu_item WHERE item_name = N'Beef Stew Bowl';
SELECT @item_spring = menu_item_id FROM menu_item WHERE item_name = N'Spring Roll (Gỏi cuốn)';
SELECT @item_cake = menu_item_id FROM menu_item WHERE item_name = N'Carrot Cake Slice';
SELECT @item_coffee = menu_item_id FROM menu_item WHERE item_name = N'Black Coffee';

------------------------------------------------------------
-- 11) voucher
------------------------------------------------------------
INSERT INTO voucher (voucher_code, voucher_name, discount_type, discount_value, quantity, start_date, end_date, status)
VALUES
(N'WELCOME10', N'Welcome 10% OFF', N'Percent', 10, 100, '2025-01-01', '2025-12-31', N'Active'),
(N'FLAT50', N'Flat 50K OFF', N'Amount', 50000, 50, '2025-06-01', '2025-12-31', N'Active');

SELECT @voucher1 = voucher_id FROM voucher WHERE voucher_code = N'WELCOME10';
SELECT @voucher2 = voucher_id FROM voucher WHERE voucher_code = N'FLAT50';

------------------------------------------------------------
-- 12) reservation
------------------------------------------------------------
INSERT INTO reservation (customer_id, table_id, reservation_date, reservation_time, party_size, status)
VALUES
(@cust_1, @table_3, '2025-10-30', '19:00', 5, N'Pending'),
(@cust_2, @table_2, '2025-10-25', '12:30', 2, N'Confirmed');

SELECT @res1 = reservation_id FROM reservation WHERE customer_id = @cust_1 AND table_id = @table_3;
SELECT @res2 = reservation_id FROM reservation WHERE customer_id = @cust_2 AND table_id = @table_2;

------------------------------------------------------------
-- 15) recipe_item
------------------------------------------------------------
INSERT INTO recipe_item (recipe_id, ingredient_id, quantity, unit, note, status)
VALUES
(@rec_grilled, @ing_beef, 0.4, N'kg', N'Bò cho 1 đĩa', N'Active'),
(@rec_grilled, @ing_fishsauce, 0.03, N'l', N'Gia vị', N'Active'),
(@rec_beefstew, @ing_beef, 0.7, N'kg', N'Bò hầm', N'Active'),
(@rec_spring, @ing_shrimp, 0.15, N'kg', N'Tôm cho từng cuốn', N'Active'),
(@rec_coffee, @ing_milk, 0.05, N'l', N'Sữa cho 1 ly', N'Active');

------------------------------------------------------------
-- 16) import & import_detail
------------------------------------------------------------
INSERT INTO [import] (supplier_id, emp_id, import_date, status)
VALUES
(@sup_1, @emp_chef, GETDATE(), N'Completed'),
(@sup_2, @emp_chef, GETDATE(), N'Pending');

SELECT @import1 = import_id FROM [import] WHERE supplier_id = @sup_1 AND emp_id = @emp_chef AND status = N'Completed';
SELECT @import2 = import_id FROM [import] WHERE supplier_id = @sup_2 AND emp_id = @emp_chef AND status = N'Pending';

INSERT INTO import_detail (import_id, ingredient_id, quantity, unit_price, total_price)
VALUES
(@import1, @ing_beef, 20, 175000.00, 3250000.00),
(@import1, @ing_lettuce, 50, 12000.00, 600000.00),
(@import2, @ing_shrimp, 10, 155000.00, 1550000.00);

------------------------------------------------------------
-- 17) order
------------------------------------------------------------
INSERT INTO [order] (reservation_id, emp_id, voucher_id, order_date, order_time, payment_method, status)
VALUES
(@res1, @emp_waiter, @voucher1, CAST(GETDATE() AS DATE), CAST(GETDATE() AS TIME), N'Cash', N'Completed'),
(@res2, @emp_cashier, @voucher2, CAST(GETDATE() AS DATE), CAST(GETDATE() AS TIME), N'Credit Card', N'Pending');

SELECT @order1 = order_id FROM [order] WHERE reservation_id = @res1;
SELECT @order2 = order_id FROM [order] WHERE reservation_id = @res2;

------------------------------------------------------------
-- 18) order_item
------------------------------------------------------------
INSERT INTO order_item (order_id, menu_item_id, unit_price, quantity)
VALUES
(@order1, @item_grilled, 120000, 2),  -- 2 phần gà nướng
(@order1, @item_spring, 45000, 3),    -- 3 phần gỏi cuốn
(@order1, @item_coffee, 30000, 2),    -- 2 ly cà phê đen
(@order2, @item_beef, 150000, 1),     -- 1 tô bò hầm
(@order2, @item_cake, 45000, 2);      -- 2 miếng bánh cà rốt

COMMIT TRANSACTION;
GO

PRINT N'✅ Dữ liệu mẫu cho database RestaurantManagement đã được chèn thành công!';
GO
