# CPUT Finder

CPUT Finder is a campus navigation and student-record web application. It
uses a static HTML/CSS/JavaScript frontend, a Spring Boot REST API, and a
MySQL database running through XAMPP.

## Application structure

```text
XAMPP Apache
    |
    v
Static frontend (HTML/CSS/JavaScript)
    |
    v
Spring Boot REST API (http://localhost:8080)
    |
    v
MySQL/MariaDB database (localhost:3306)
```

## Requirements

Install or have the following available:

- XAMPP with Apache and MySQL
- Java JDK 21
- Apache Maven
- A modern web browser

The backend uses MySQL. Derby is not required for this project.

## Project locations

Backend Maven project:

```text
/Users/paci/NetBeansProjects/cputFinder
```

Frontend served by XAMPP:

```text
/Applications/XAMPP/xamppfiles/htdocs/web drafts /views/html viwes
```

The frontend pages are inside the `pages` folder. The login page is:

```text
/Applications/XAMPP/xamppfiles/htdocs/web drafts /views/html viwes/pages/index.html
```

If the project is moved to another computer, copy the frontend folder into
the XAMPP `htdocs` directory and update the browser URL to match its folder
name.

## First-time database setup

1. Open the XAMPP Control Panel.
2. Start **MySQL**.
3. Open phpMyAdmin at <http://localhost/phpmyadmin>.
4. Select **Import**.
5. Import:

   ```text
   /Users/paci/NetBeansProjects/cputFinder/sql/cput_finder.sql
   ```

The SQL script creates the `cput_finder` database, tables, relationships,
indexes, and marked development seed records.

For an existing database, import
`sql/migrations/2026-11-11-create-cafeteria-menu-items.sql` in phpMyAdmin. This
creates the `Cafeteria_Menu_Item` table and seeds the current Breakfast, Lunch,
and Dinner entries without duplicating existing items. Administrators can
change the menu in the **Menu Items** page; students see the current database
entries on the Cafeteria page.

The default database connection is:

```text
Host:     localhost
Port:     3306
Database: cput_finder
Username: root
Password: empty
```

If the MySQL account has a password, configure it before starting the API:

### macOS/Linux

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=cput_finder
export DB_USER=root
export DB_PASSWORD=your_mysql_password
```

### Windows PowerShell

```powershell
$env:DB_HOST = "localhost"
$env:DB_PORT = "3306"
$env:DB_NAME = "cput_finder"
$env:DB_USER = "root"
$env:DB_PASSWORD = "your_mysql_password"
```

## Start the backend

Open a terminal and change to the Maven project directory:

```bash
cd "/Users/paci/NetBeansProjects/cputFinder"
```

Start the Spring Boot API:

```bash
cd "/Users/paci/NetBeansProjects/CputFinder - backend"
mvn -Dmaven.test.skip=true spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

Keep this terminal running while using the website.

### Start the packaged JAR instead

To build the application:

```bash
mvn -Dmaven.test.skip=true package
```

Then run the packaged application:

```bash
java -jar target/cputFinder-1.0-SNAPSHOT.jar
```

## Start the frontend

1. Open XAMPP.
2. Start **Apache**.
3. Keep the Spring Boot API running on port `8080`.
4. Open the login page in a browser:

   <http://localhost/web%20drafts%20/views/html%20viwes/pages/index.html>

Do not open the HTML file directly with `file://`. The website should be
opened through Apache so that its requests to the API work correctly.

## Using the application

### Student account

Select **Register** on the login page and create a student account. Then log
in using the registered email address and password.

Student features include:

- Campus map
- Buildings and rooms
- Timetable
- Saved places
- Cafeteria information
- Profile information

### Administrator account

The development administrator account is:

```text
Email:    admin@cputfinder.local
Password: CPUTFinderAdmin@26!
```

The administrator can manage student records and the connected campus
records, including buildings, rooms, lecturers, courses, food stores, and
schedules.

The administrator session is held in memory by the API. Restarting Spring
Boot logs the administrator out and requires another login.

## REST API endpoints

The API base URL is:

```text
http://localhost:8080/api
```

Available endpoints include:

| Feature | Endpoint |
| --- | --- |
| Register | `POST /register` |
| Login | `POST /login` |
| Students | `/students` |
| Buildings | `/buildings` |
| Rooms | `/rooms` |
| Lecturers | `/lecturers` |
| Courses | `/courses` |
| Food stores | `/food-stores` |
| Cafeteria menu | `GET /menu-items` (public); `POST`, `PUT`, and `DELETE /menu-items` (administrator) |
| Schedules | `/schedules` |
| Saved places | `/saved-places` |

Most management create, update, and delete requests require the
`X-Admin-Session` header returned by administrator login.

## Seed data

The SQL file contains development records marked with `SEED-` identifiers.
They are included to make the application demonstrable immediately after
database setup.

When real assignment data is available, remove only the marked development
records with the appropriate statements, for example:

```sql
DELETE FROM Saved_Place WHERE saved_name LIKE 'SEED-%';
DELETE FROM Schedule WHERE schedule_code LIKE 'SEED-%';
DELETE FROM Food_Store WHERE store_code LIKE 'SEED-%';
DELETE FROM Course WHERE course_code LIKE 'SEED-%';
DELETE FROM Lecturer WHERE lecturer_number LIKE 'SEED-%';
DELETE FROM Room WHERE room_number LIKE 'SEED-%';
DELETE FROM Building WHERE building_code LIKE 'SEED-%';
DELETE FROM Student WHERE student_number LIKE 'SEED-%';
```

Back up the database before deleting data.

## Troubleshooting

### `Communications link failure` or database connection errors

- Confirm MySQL is running in XAMPP.
- Confirm the database is named `cput_finder`.
- Confirm the username and password match the environment variables.
- Confirm MySQL is using port `3306`.

### The browser shows “Could not reach the server”

- Confirm Spring Boot is running.
- Open <http://localhost:8080> or check the terminal for startup errors.
- Confirm no other application is using port `8080`.

### CORS or failed API requests

- Open the frontend through Apache, not `file://`.
- Confirm the frontend JavaScript uses `http://localhost:8080/api`.
- Confirm Apache and Spring Boot are both running.

### The administrator page is denied

- Log in with the administrator credentials above.
- Open the page again after logging in.
- If the API was restarted, log in again because the old administrator token
  is no longer valid.

## Build and tests

Build the production JAR without compiling the current database integration
tests:

```bash
mvn -Dmaven.test.skip=true package
```

Run the Maven tests when the required test database configuration is
available:

```bash
mvn test
```

## Related documentation

- [SETUP.md](SETUP.md)
- [Database SQL script](sql/cput_finder.sql)
