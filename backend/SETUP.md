# CPUT Finder local setup

## 1. Create the XAMPP database

Start **MySQL** in XAMPP, open phpMyAdmin, choose **Import**, and import
`sql/cput_finder.sql`.

The script creates the `cput_finder` database and the `Student` table. The
default connection expects the XAMPP MySQL account to be `root` with no
password. If your account is different, set these environment variables
before starting Spring Boot:

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=cput_finder
export DB_USER=root
export DB_PASSWORD=your_mysql_password
```

## 2. Start the Java API

From the backend project directory:

```bash
mvn -Dmaven.test.skip=true spring-boot:run
```

The API runs at `http://localhost:8080`.

## 3. Open the website through XAMPP

Start **Apache** in XAMPP and open:

`http://localhost/CPUT%20%20District%20Navigation%20map%20website%202/pages/login.html`

The registration and login forms call:

- `POST http://localhost:8080/api/register`
- `POST http://localhost:8080/api/login`

Do not open the HTML directly with `file://`; use Apache so the browser
origin and CORS configuration are correct.

## Admin student-record access

The student-record management page requires an administrator login. The
development account is:

```text
Email: admin@cputfinder.local
Password: CPUTFinderAdmin@26!
```

For deployment, override the default with a BCrypt hash by setting
`ADMIN_PASSWORD_HASH`. The email can be changed with `ADMIN_EMAIL`. Student
accounts cannot access `/api/students`; those endpoints require the
server-issued admin session token.
