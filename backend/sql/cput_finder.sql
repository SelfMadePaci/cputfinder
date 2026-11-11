CREATE DATABASE IF NOT EXISTS cput_finder
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE cput_finder;

CREATE TABLE IF NOT EXISTS Building (
    building_id INT NOT NULL AUTO_INCREMENT,
    building_name VARCHAR(150) NOT NULL,
    building_code VARCHAR(30) NOT NULL,
    description VARCHAR(500) NOT NULL,
    PRIMARY KEY (building_id),
    UNIQUE KEY uq_building_code (building_code),
    KEY idx_building_name (building_name)
);

CREATE TABLE IF NOT EXISTS Student (
    student_id INT NOT NULL AUTO_INCREMENT,
    student_number VARCHAR(30) NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    student_email VARCHAR(150) NOT NULL,
    phone_number VARCHAR(30) NULL,
    password VARCHAR(100) NOT NULL,
    PRIMARY KEY (student_id),
    UNIQUE KEY uq_student_number (student_number),
    UNIQUE KEY uq_student_email (student_email)
);

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (username),
    UNIQUE KEY uq_users_username (username)
);

CREATE TABLE IF NOT EXISTS Room (
    room_id INT NOT NULL AUTO_INCREMENT,
    room_number VARCHAR(30) NOT NULL,
    floor_number INT NOT NULL,
    building_id INT NOT NULL,
    PRIMARY KEY (room_id),
    UNIQUE KEY uq_room_building (room_number, building_id),
    KEY idx_room_building (building_id),
    CONSTRAINT fk_room_building FOREIGN KEY (building_id) REFERENCES Building(building_id)
);

CREATE TABLE IF NOT EXISTS Office (
    office_id INT NOT NULL AUTO_INCREMENT,
    office_number VARCHAR(30) NOT NULL,
    floor_number INT NOT NULL DEFAULT 0,
    building_id INT NULL,
    PRIMARY KEY (office_id),
    UNIQUE KEY uq_office_number (office_number),
    KEY idx_office_building (building_id),
    CONSTRAINT fk_office_building FOREIGN KEY (building_id) REFERENCES Building(building_id)
);

CREATE TABLE IF NOT EXISTS Lecturer (
    lecturer_id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    email VARCHAR(150) NOT NULL,
    office_id INT NULL,
    PRIMARY KEY (lecturer_id),
    UNIQUE KEY uq_lecturer_email (email),
    KEY idx_lecturer_office (office_id),
    CONSTRAINT fk_lecturer_office FOREIGN KEY (office_id) REFERENCES Office(office_id)
);

CREATE TABLE IF NOT EXISTS Course (
    course_id INT NOT NULL AUTO_INCREMENT,
    course_code VARCHAR(30) NOT NULL,
    course_name VARCHAR(150) NOT NULL,
    lecturer_id INT NOT NULL,
    PRIMARY KEY (course_id),
    UNIQUE KEY uq_course_code (course_code),
    KEY idx_course_lecturer (lecturer_id),
    CONSTRAINT fk_course_lecturer FOREIGN KEY (lecturer_id) REFERENCES Lecturer(lecturer_id)
);

CREATE TABLE IF NOT EXISTS Food_Store (
    store_id INT NOT NULL AUTO_INCREMENT,
    store_name VARCHAR(150) NOT NULL,
    operating_hours VARCHAR(100) NOT NULL,
    food_type VARCHAR(100) NOT NULL,
    building_id INT NOT NULL,
    PRIMARY KEY (store_id),
    KEY idx_food_store_building (building_id),
    CONSTRAINT fk_food_store_building FOREIGN KEY (building_id) REFERENCES Building(building_id)
);

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

CREATE TABLE IF NOT EXISTS Saved_Place (
    saved_id INT NOT NULL AUTO_INCREMENT,
    student_id INT NOT NULL,
    building_id INT NOT NULL,
    saved_name VARCHAR(150) NOT NULL,
    PRIMARY KEY (saved_id),
    UNIQUE KEY uq_saved_place (student_id, building_id),
    KEY idx_saved_place_student (student_id),
    KEY idx_saved_place_building (building_id),
    CONSTRAINT fk_saved_place_student FOREIGN KEY (student_id) REFERENCES Student(student_id),
    CONSTRAINT fk_saved_place_building FOREIGN KEY (building_id) REFERENCES Building(building_id)
);

CREATE TABLE IF NOT EXISTS Schedule (
    schedule_id INT NOT NULL AUTO_INCREMENT,
    student_id INT NOT NULL,
    course_id INT NOT NULL,
    room_id INT NOT NULL,
    class_date DATE NOT NULL,
    starting_time TIME NOT NULL,
    ending_time TIME NOT NULL,
    PRIMARY KEY (schedule_id),
    KEY idx_schedule_student_date (student_id, class_date),
    KEY idx_schedule_course (course_id),
    KEY idx_schedule_room (room_id),
    CONSTRAINT fk_schedule_student FOREIGN KEY (student_id) REFERENCES Student(student_id),
    CONSTRAINT fk_schedule_course FOREIGN KEY (course_id) REFERENCES Course(course_id),
    CONSTRAINT fk_schedule_room FOREIGN KEY (room_id) REFERENCES Room(room_id)
);

CREATE TABLE IF NOT EXISTS Preference (
    preference_id INT NOT NULL AUTO_INCREMENT,
    student_id INT NOT NULL,
    accessible_route VARCHAR(255) NOT NULL,
    notification VARCHAR(150) NOT NULL,
    PRIMARY KEY (preference_id),
    UNIQUE KEY uq_preference_student (student_id),
    KEY idx_preference_student (student_id),
    CONSTRAINT fk_preference_student FOREIGN KEY (student_id) REFERENCES Student(student_id)
);

CREATE TABLE IF NOT EXISTS Route (
    route_id INT NOT NULL AUTO_INCREMENT,
    start_building_id INT NOT NULL,
    end_building_id INT NOT NULL,
    estimated_time INT NOT NULL,
    description VARCHAR(500) NOT NULL,
    PRIMARY KEY (route_id),
    KEY idx_route_start (start_building_id),
    KEY idx_route_end (end_building_id),
    CONSTRAINT fk_route_start_building FOREIGN KEY (start_building_id) REFERENCES Building(building_id),
    CONSTRAINT fk_route_end_building FOREIGN KEY (end_building_id) REFERENCES Building(building_id)
);

-- ASSIGNMENT SEED DATA
-- These records are clearly marked and can be removed later with:
-- DELETE FROM Student WHERE student_number LIKE 'SEED-%';
--
-- Seed login password for every record below: CPUTFinder2026!
-- The SEED- prefix and .invalid email domain keep this sample data separate
-- from real student records.
INSERT INTO Student (student_number, full_name, student_email, phone_number, password)
SELECT seed.student_number, seed.full_name, seed.student_email, seed.phone_number, seed.password
FROM (
    SELECT 'SEED-0001' AS student_number, 'Amina Jacobs' AS full_name, 'seed+amina.jacobs@example.invalid' AS student_email, '+27 82 111 0001' AS phone_number, '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS' AS password
    UNION ALL SELECT 'SEED-0002', 'Bongani Maseko', 'seed+bongani.maseko@example.invalid', '+27 73 222 0002', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0003', 'Chantelle Adams', 'seed+chantelle.adams@example.invalid', '+27 81 333 0003', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0004', 'David Ndlovu', 'seed+david.ndlovu@example.invalid', '+27 71 444 0004', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0005', 'Esethu Williams', 'seed+esethu.williams@example.invalid', '+27 82 555 0005', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0006', 'Fatima Daniels', 'seed+fatima.daniels@example.invalid', '+27 84 666 0006', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0007', 'Gavin Petersen', 'seed+gavin.petersen@example.invalid', '+27 79 777 0007', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0008', 'Hlelo Mthembu', 'seed+hlelo.mthembu@example.invalid', '+27 76 888 0008', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0009', 'Imraan Khan', 'seed+imraan.khan@example.invalid', '+27 83 999 0009', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0010', 'Jade Williams', 'seed+jade.williams@example.invalid', '+27 86 101 0010', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0011', 'Khumo Dlamini', 'seed+khumo.dlamini@example.invalid', '+27 64 111 0011', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0012', 'Lerato Mokoena', 'seed+lerato.mokoena@example.invalid', '+27 63 222 0012', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0013', 'Mandla Jantjies', 'seed+mandla.jantjies@example.invalid', '+27 78 333 0013', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0014', 'Nadia September', 'seed+nadia.september@example.invalid', '+27 72 444 0014', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
    UNION ALL SELECT 'SEED-0015', 'Oliver Smith', 'seed+oliver.smith@example.invalid', '+27 65 555 0015', '$2a$12$mhhPj/Ky8/g4uZ8R7ZbrbeE1Ql.sGARDQuZnDb4wMGIynU7PGbxNS'
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM Student existing
    WHERE existing.student_number = seed.student_number
);

-- LECTURER SEED DATA
-- Remove only these sample lecturers later with:
-- DELETE FROM Lecturer WHERE email LIKE 'seed+%@example.invalid';
INSERT INTO Lecturer (first_name, last_name, email)
SELECT seed.first_name, seed.last_name, seed.email
FROM (
    SELECT 'Nomsa' AS first_name, 'Mbeki' AS last_name, 'seed+nomsa.mbeki@example.invalid' AS email
    UNION ALL SELECT 'Thabo', 'Molefe', 'seed+thabo.molefe@example.invalid'
    UNION ALL SELECT 'Leigh', 'Daniels', 'seed+leigh.daniels@example.invalid'
    UNION ALL SELECT 'Yusuf', 'Davids', 'seed+yusuf.davids@example.invalid'
    UNION ALL SELECT 'Rochelle', 'Peters', 'seed+rochelle.peters@example.invalid'
) AS seed
WHERE NOT EXISTS (SELECT 1 FROM Lecturer existing WHERE existing.email = seed.email);

-- COURSE SEED DATA
-- Remove only these sample courses later with:
-- DELETE FROM Course WHERE course_code LIKE 'SEED-%';
INSERT INTO Course (course_code, course_name, lecturer_id)
SELECT seed.course_code, seed.course_name, l.lecturer_id
FROM (
    SELECT 'SEED-PRG251S' AS course_code, 'Programming 251' AS course_name, 'seed+nomsa.mbeki@example.invalid' AS lecturer_email
    UNION ALL SELECT 'SEED-DBD221S', 'Database Design 221', 'seed+thabo.molefe@example.invalid'
    UNION ALL SELECT 'SEED-CNF221S', 'Computer Networks Fundamentals 221', 'seed+leigh.daniels@example.invalid'
    UNION ALL SELECT 'SEED-PPR221S', 'Project Practice 221', 'seed+yusuf.davids@example.invalid'
    UNION ALL SELECT 'SEED-WEB251S', 'Web Development 251', 'seed+rochelle.peters@example.invalid'
) AS seed
JOIN Lecturer l ON l.email = seed.lecturer_email
WHERE NOT EXISTS (SELECT 1 FROM Course existing WHERE existing.course_code = seed.course_code);

-- ROOM SEED DATA
-- Remove only these sample rooms later with:
-- DELETE FROM Room WHERE room_number LIKE 'SEED-%';
INSERT INTO Room (room_number, floor_number, building_id)
SELECT seed.room_number, seed.floor_number, b.building_id
FROM (
    SELECT 'SEED-LIB-01' AS room_number, 1 AS floor_number, 'SEED-LIB' AS building_code
    UNION ALL SELECT 'SEED-LIB-02', 1, 'SEED-LIB'
    UNION ALL SELECT 'SEED-ENG-101', 1, 'SEED-ENG'
    UNION ALL SELECT 'SEED-ENG-201', 2, 'SEED-ENG'
    UNION ALL SELECT 'SEED-SC-101', 1, 'SEED-SC'
    UNION ALL SELECT 'SEED-ADM-001', 0, 'SEED-ADM'
    UNION ALL SELECT 'SEED-BUS-201', 2, 'SEED-BUS'
    UNION ALL SELECT 'SEED-IT-LAB1', 1, 'SEED-IT'
    UNION ALL SELECT 'SEED-IT-LAB2', 1, 'SEED-IT'
    UNION ALL SELECT 'SEED-DES-101', 1, 'SEED-DES'
    UNION ALL SELECT 'SEED-HWC-001', 0, 'SEED-HWC'
    UNION ALL SELECT 'SEED-CAF-001', 0, 'SEED-CAF'
    UNION ALL SELECT 'SEED-SPT-001', 0, 'SEED-SPT'
    UNION ALL SELECT 'SEED-LHA-001', 0, 'SEED-LHA'
    UNION ALL SELECT 'SEED-IH-101', 1, 'SEED-IH'
) AS seed
JOIN Building b ON b.building_code = seed.building_code
WHERE NOT EXISTS (
    SELECT 1 FROM Room existing
    WHERE existing.room_number = seed.room_number
);

-- BUILDING SEED DATA
-- Remove only these sample buildings later with:
-- DELETE FROM Building WHERE building_code LIKE 'SEED-%';
INSERT INTO Building (building_name, building_code, description)
SELECT seed.building_name, seed.building_code, seed.description
FROM (
    SELECT 'CPUT District Six Library' AS building_name, 'SEED-LIB' AS building_code, 'Study spaces, computers, books, and academic resources.' AS description
    UNION ALL SELECT 'Engineering Building', 'SEED-ENG', 'Engineering classrooms, laboratories, and practical facilities.'
    UNION ALL SELECT 'Student Centre', 'SEED-SC', 'Student services, support offices, and common areas.'
    UNION ALL SELECT 'Administration Building', 'SEED-ADM', 'Central administration and student registration services.'
    UNION ALL SELECT 'Business Faculty Building', 'SEED-BUS', 'Business and management lecture rooms and offices.'
    UNION ALL SELECT 'Information Technology Building', 'SEED-IT', 'Computer laboratories and information technology classrooms.'
    UNION ALL SELECT 'Design and Media Building', 'SEED-DES', 'Design studios, media laboratories, and creative workspaces.'
    UNION ALL SELECT 'Health and Wellness Centre', 'SEED-HWC', 'Student health, counselling, and wellness services.'
    UNION ALL SELECT 'Cafeteria', 'SEED-CAF', 'Food vendors, seating, and student refreshment facilities.'
    UNION ALL SELECT 'Sports Centre', 'SEED-SPT', 'Indoor recreation, fitness, and student sport facilities.'
    UNION ALL SELECT 'Lecture Hall A', 'SEED-LHA', 'Large lecture venue for faculty-wide classes and events.'
    UNION ALL SELECT 'Lecture Hall B', 'SEED-LHB', 'Lecture venue with accessible seating and presentation equipment.'
    UNION ALL SELECT 'Innovation Hub', 'SEED-IH', 'Collaborative project, entrepreneurship, and innovation space.'
    UNION ALL SELECT 'Security Office', 'SEED-SEC', 'Campus security and lost-property services.'
    UNION ALL SELECT 'Transport Office', 'SEED-TRN', 'Campus transport information and shuttle coordination.'
) AS seed
WHERE NOT EXISTS (
    SELECT 1 FROM Building existing
    WHERE existing.building_code = seed.building_code
);

-- FOOD STORE SEED DATA
-- Remove only these sample food stores later with:
-- DELETE FROM Food_Store WHERE store_name LIKE 'SEED-%';
INSERT INTO Food_Store (store_name, operating_hours, food_type, building_id)
SELECT seed.store_name, seed.operating_hours, seed.food_type, b.building_id
FROM (
    SELECT 'SEED-Campus Cafeteria' AS store_name, '07:00-18:00' AS operating_hours, 'Hot meals and snacks' AS food_type, 'SEED-CAF' AS building_code
    UNION ALL SELECT 'SEED-Fresh Bites', '08:00-16:00', 'Sandwiches and salads', 'SEED-SC'
    UNION ALL SELECT 'SEED-Coffee Corner', '07:30-17:00', 'Coffee and pastries', 'SEED-LIB'
    UNION ALL SELECT 'SEED-Pizza Station', '10:00-19:00', 'Pizza and fast food', 'SEED-CAF'
    UNION ALL SELECT 'SEED-Veggie Kitchen', '09:00-17:00', 'Vegetarian meals', 'SEED-HWC'
) AS seed
JOIN Building b ON b.building_code = seed.building_code
WHERE NOT EXISTS (SELECT 1 FROM Food_Store existing WHERE existing.store_name = seed.store_name);

-- CAFETERIA MENU SEED DATA
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

-- PREFERENCE SEED DATA
-- Remove only these sample preferences later with:
-- DELETE FROM Preference WHERE student_id IN (SELECT student_id FROM Student WHERE student_number LIKE 'SEED-%');
INSERT INTO Preference (student_id, accessible_route, notification)
SELECT s.student_id, seed.accessible_route, seed.notification
FROM (
    SELECT 'SEED-0001' AS student_number, 'Library -> Engineering via the south access ramp' AS accessible_route, 'Email and SMS updates' AS notification
    UNION ALL SELECT 'SEED-0002', 'Student Centre -> Business Faculty via the main walkway', 'Email reminders'
    UNION ALL SELECT 'SEED-0003', 'Library -> Cafeteria via the accessible route', 'Push notifications'
) AS seed
JOIN Student s ON s.student_number = seed.student_number
WHERE NOT EXISTS (
    SELECT 1 FROM Preference existing
    WHERE existing.student_id = s.student_id
);

-- SCHEDULE SEED DATA
-- Remove only these sample schedules later with:
-- DELETE FROM Schedule WHERE schedule_id IN (
--   SELECT schedule_id FROM (SELECT schedule_id FROM Schedule
--   WHERE student_id IN (SELECT student_id FROM Student WHERE student_number LIKE 'SEED-%')) AS marked
-- );
INSERT INTO Schedule (student_id, course_id, room_id, class_date, starting_time, ending_time)
SELECT s.student_id, c.course_id, r.room_id, seed.class_date, seed.starting_time, seed.ending_time
FROM (
    SELECT 'SEED-0001' AS student_number, 'SEED-PRG251S' AS course_code, 'SEED-ENG-101' AS room_number, '2026-10-05' AS class_date, '08:30:00' AS starting_time, '09:30:00' AS ending_time
    UNION ALL SELECT 'SEED-0001', 'SEED-DBD221S', 'SEED-IT-LAB1', '2026-10-06', '09:30:00', '10:30:00'
    UNION ALL SELECT 'SEED-0001', 'SEED-CNF221S', 'SEED-LIB-01', '2026-10-07', '10:30:00', '11:30:00'
    UNION ALL SELECT 'SEED-0001', 'SEED-PPR221S', 'SEED-SC-101', '2026-10-08', '11:30:00', '12:30:00'
    UNION ALL SELECT 'SEED-0001', 'SEED-WEB251S', 'SEED-DES-101', '2026-10-09', '13:30:00', '14:30:00'
) AS seed
JOIN Student s ON s.student_number = seed.student_number
JOIN Course c ON c.course_code = seed.course_code
JOIN Room r ON r.room_number = seed.room_number
WHERE NOT EXISTS (
    SELECT 1 FROM Schedule existing
    WHERE existing.student_id = s.student_id
      AND existing.course_id = c.course_id
      AND existing.class_date = seed.class_date
      AND existing.starting_time = seed.starting_time
);

-- SAVED PLACE SEED DATA
-- Remove only these sample saved places later with:
-- DELETE FROM Saved_Place WHERE saved_name LIKE 'SEED-%';
INSERT INTO Saved_Place (student_id, building_id, saved_name)
SELECT s.student_id, b.building_id, seed.saved_name
FROM (
    SELECT 'SEED-0001' AS student_number, 'SEED-LIB' AS building_code, 'SEED-Library' AS saved_name
    UNION ALL SELECT 'SEED-0001', 'SEED-ENG', 'SEED-Engineering'
    UNION ALL SELECT 'SEED-0001', 'SEED-CAF', 'SEED-Cafeteria'
) AS seed
JOIN Student s ON s.student_number = seed.student_number
JOIN Building b ON b.building_code = seed.building_code
WHERE NOT EXISTS (
    SELECT 1 FROM Saved_Place existing
    WHERE existing.student_id = s.student_id AND existing.building_id = b.building_id
);

-- ROUTE SEED DATA
-- Remove only these sample routes later with:
-- DELETE FROM Route WHERE description LIKE 'SEED-%';
INSERT INTO Route (start_building_id, end_building_id, estimated_time, description)
SELECT start_b.building_id, end_b.building_id, seed.estimated_time, seed.description
FROM (
    SELECT 'SEED-LIB' AS start_code, 'SEED-ENG' AS end_code, 8 AS estimated_time, 'SEED-Accessible route across the covered walkway.' AS description
    UNION ALL SELECT 'SEED-ENG', 'SEED-LIB', 8, 'SEED-Return route with lift access and wide corridors.'
    UNION ALL SELECT 'SEED-SC', 'SEED-CAF', 5, 'SEED-Short route via the student centre courtyard.'
    UNION ALL SELECT 'SEED-ADM', 'SEED-BUS', 6, 'SEED-Direct administrative route through the central plaza.'
) AS seed
JOIN Building start_b ON start_b.building_code = seed.start_code
JOIN Building end_b ON end_b.building_code = seed.end_code
WHERE NOT EXISTS (
    SELECT 1 FROM Route existing
    WHERE existing.start_building_id = start_b.building_id
      AND existing.end_building_id = end_b.building_id
);
