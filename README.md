# CPUT Finder

CPUT Finder is a campus navigation and student-record web application. The
frontend is served by XAMPP Apache and communicates with a Spring Boot REST
API. The API stores data in MySQL/MariaDB supplied by XAMPP.

## Technologies used

- Java 21
- Spring Boot
- Maven
- MySQL/MariaDB (via XAMPP)
- HTML5
- CSS3
- JavaScript (vanilla)
- Apache XAMPP

## Application architecture

The application uses a three-layer structure:

```text
Browser (HTML/CSS/JavaScript)
    |
    v
XAMPP Apache (serves the frontend pages)
    |
    v
Spring Boot REST API (http://localhost:8080)
    |
    v
MySQL database (cput_finder on localhost:3306)
```

The frontend is static and fetches data from the backend through REST API
calls. The Spring Boot application handles authentication, student records,
and all campus-management CRUD operations.

## GitHub repository

The complete project repository is available here:

```text
https://github.com/SelfMadePaci/cputfinder
```

## Project locations

Frontend:

```text
/Applications/XAMPP/xamppfiles/htdocs/web drafts /views/CputFinder - Front end
```

Backend:

```text
/Users/paci/NetBeansProjects/CputFinder - backend
```

Database script:

```text
/Users/paci/NetBeansProjects/CputFinder - backend/sql/cput_finder.sql
```

Project folder structure:

```text
CputFinder - Front end/
├── css/
├── images/
├── js/
├── pages/
├── README.md
└── .git/

CputFinder - backend/
├── src/
├── sql/
├── pom.xml
├── README.md
├── SETUP.md
└── target/             (generated locally; not committed)
```

If you move the project to another computer, copy this frontend folder into
the XAMPP `htdocs` folder and update the backend path in the commands below.

# macOS / Linux Setup

## 1. Set up the database

1. Open XAMPP.
2. Start **MySQL**.
3. Open <http://localhost/phpmyadmin>.
4. Select **Import**.
5. Import `sql/cput_finder.sql` from the backend project.
The script creates the `cput_finder` database, tables, relationships, indexes,
and marked development seed records.

The default database settings are:

```text
Host:     localhost
Port:     3306
Database: cput_finder
Username: root
Password: empty
```

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=cput_finder
export DB_USER=root
export DB_PASSWORD=your_mysql_password
```

## 2. Start the backend

Open a terminal and run:

```bash
cd "/Users/paci/NetBeansProjects/CputFinder - backend"
mvn -Dmaven.test.skip=true spring-boot:run
```

The REST API runs at:

```text
http://localhost:8080
```

Keep this terminal running while using the website.

Alternatively, build and run the packaged JAR:

```bash
mvn -Dmaven.test.skip=true package
java -jar target/cputFinder-1.0-SNAPSHOT.jar
```

## 3. Start the frontend

1. In XAMPP, start **Apache**.
2. Keep the Spring Boot backend running on port `8080`.
3. Open the login page:

   <http://localhost/web%20drafts%20/views/CputFinder%20-%20Front%20end/pages/index.html>

Do not open the HTML files directly with `file://`. Use Apache so the browser
can communicate with the API correctly.

# Windows Setup

## 1. Set up the database

1. Open XAMPP.
2. Start **MySQL**.
3. Open <http://localhost/phpmyadmin>.
4. Select **Import**.
5. Import `sql/cput_finder.sql` from the backend project.

The default database settings are:

```text
Host:     localhost
Port:     3306
Database: cput_finder
Username: root
Password: empty
```

Use PowerShell to set the environment variables if needed:

```powershell
$env:DB_HOST = "localhost"
$env:DB_PORT = "3306"
$env:DB_NAME = "cput_finder"
$env:DB_USER = "root"
$env:DB_PASSWORD = ""
```

## 2. Start the backend

Open PowerShell and run:

```powershell
cd "C:\Users\YourName\NetBeansProjects\CputFinder - backend"
mvn -Dmaven.test.skip=true spring-boot:run
```

The REST API runs at:

```text
http://localhost:8080
```

Alternatively, build and run the packaged JAR:

```powershell
mvn -Dmaven.test.skip=true package
java -jar target\cputFinder-1.0-SNAPSHOT.jar
```

## 3. Start the frontend

1. Start **Apache** in XAMPP.
2. Keep the backend running on port `8080`.
3. Open the website in a browser using the correct XAMPP path. A typical URL is:

```text
http://localhost/CputFinder%20-%20Front%20end/pages/index.html
```

If your project folder is in a different XAMPP folder, use the matching path from
that folder instead. Do not open the HTML files directly with `file://`.

## Application features

The frontend includes the following major user-facing sections:

- Login and registration
- Student dashboard and profile
- Campus map
- Building and room management
- Lecturer management
- Course management
- Food store management
- Schedule and timetable views
- Saved places
- Settings and administrator tools

## REST API endpoints

The API base URL is `http://localhost:8080/api`.

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
| Schedules | `/schedules` |
| Saved places | `/saved-places` |

Most admin create, update, and delete requests require the `X-Admin-Session`
header returned by the administrator login endpoint.

## Logging in

### Student

Select **Register**, create a student account, and then log in with the
registered email and password.

### Administrator

The administrator account is configured through environment variables. Do not
commit administrator credentials to the repository. Set `ADMIN_EMAIL` and
`ADMIN_PASSWORD_HASH` in the backend environment before starting the API.

The administrator can manage student records, buildings, rooms, lecturers,
courses, food stores, and schedules.

The administrator session is stored in memory. If Spring Boot is restarted,
log in again.

## Main features

- Student registration and login
- Student records CRUD
- Buildings CRUD
- Rooms CRUD linked to buildings
- Lecturers CRUD
- Courses CRUD linked to lecturers
- Food stores CRUD linked to buildings
- Schedules and student timetables
- Student saved places
- Search, feedback, and validation messages

## Seed data

The SQL file includes development records marked with `SEED-`. These records
are there for demonstration and testing. Back up the database before deleting
them.

To remove the marked records:

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

## Troubleshooting

### Database connection error

- Confirm MySQL is running in XAMPP.
- Confirm the database name is `cput_finder`.
- Confirm MySQL is using port `3306`.
- Check `DB_USER` and `DB_PASSWORD`.

### API connection error in the browser

- Confirm the Spring Boot terminal is still running.
- Confirm the API is using port `8080`.
- Open the frontend through Apache, not `file://`.
- Confirm Apache and MySQL are running in XAMPP.

### Administrator access denied

- Log in with the administrator credentials above.
- Log in again after restarting Spring Boot.
- Confirm the API returned a successful administrator login.

# cputfinder
CPUTFinder is a desktop application designed to solve campus navigation, room discovery, and resource location for students and staff at the Cape Town Campus.
