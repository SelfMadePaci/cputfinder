[README.md](https://github.com/user-attachments/files/34696188/README.md)
# CPUT Finder

CPUT Finder is a campus navigation and student information application. This
repository contains both the static frontend and the Spring Boot backend.

## Project layout

- `frontend/` — HTML, CSS, and JavaScript pages.
- `backend/` — Spring Boot REST API, Maven build, and MySQL schema/migrations.

## Requirements

- XAMPP Apache and MySQL/MariaDB
- Java 21
- Maven

## Run locally

1. Start Apache and MySQL in XAMPP.
2. Create the database by importing `backend/sql/cput_finder.sql` in
   phpMyAdmin.
3. Configure the backend database connection if needed using `DB_HOST`,
   `DB_PORT`, `DB_NAME`, `DB_USER`, and `DB_PASSWORD`.
4. Start the API from `backend/` with `mvn spring-boot:run`.
5. Serve `frontend/` using Apache and open its `pages/index.html` page through
   the local web server.

The frontend expects the API at `http://localhost:8080/api`.
