/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.web;

/**
 *
 * @author paci
 */

import com.mycompany.cputfinder.dao.StudentDAO;
import com.mycompany.cputfinder.dao.StudentDAOImpl;
import com.mycompany.cputfinder.domain.Student;
import com.mycompany.cputfinder.util.PasswordUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bridges the static HTML pages (served from s/main/resources/static) to
 * StudentDAOImpl. The HTML/JS calls these endpoints with fetch(); this
 * class never touches JDBC directly, it just hashes/validates and delegates
 * to the DAO you already built and tested.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1", "http://localhost:80", "http://127.0.0.1:80"})
public class AuthController {

    private final StudentDAO studentDAO = new StudentDAOImpl();
    private static final Map<String, Boolean> ADMIN_SESSIONS = new ConcurrentHashMap<>();

    // ---------- request/response shapes ----------

    public record RegisterRequest(
            String studentNumber,
            String fullName,
            String studentEmail,
            String password) {}

    public record LoginRequest(
            String studentEmail,
            String password) {}

    public record AuthResponse(
            boolean success,
            String message,
            Integer studentId,
            String fullName,
            String role,
            String adminToken) {}

    // ---------- endpoints ----------

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        if (req == null || isBlank(req.studentNumber()) || isBlank(req.fullName())
                || isBlank(req.studentEmail()) || isBlank(req.password())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "Email and password are required.", null, null, null, null));
        }

        try {
            if (studentDAO.findByEmail(req.studentEmail()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new AuthResponse(false, "An account with that email already exists.", null, null, null, null));
            }

            Student student = new Student(
                    0,
                    req.studentNumber(),
                    req.fullName(),
                    req.studentEmail(),
                    PasswordUtil.hash(req.password()));

            boolean created = studentDAO.create(student);
            if (!created) {
                // StudentDAOImpl currently swallows SQLException and returns
                // false for both "bad data" and "DB unreachable" — this is
                // the best we can report until that's split out.
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body(new AuthResponse(false, "Could not reach the database. Please try again.", null, null, null, null));
            }

            return ResponseEntity.ok(
                    new AuthResponse(true, "Account created.", student.getStudentId(), student.getFullName(), "STUDENT", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(false, "Unexpected server error.", null, null, null, null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        if (req == null || isBlank(req.studentEmail()) || isBlank(req.password())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "Email and password are required.", null, null, null, null));
        }

        try {
            if (req.studentEmail().equalsIgnoreCase(adminEmail())
                    && PasswordUtil.matches(req.password(), adminPasswordHash())) {
                String token = UUID.randomUUID().toString();
                ADMIN_SESSIONS.put(token, Boolean.TRUE);
                return ResponseEntity.ok(
                        new AuthResponse(true, "Admin login successful.", null, "Administrator", "ADMIN", token));
            }

            Student student = studentDAO.findByEmail(req.studentEmail());

            if (student == null || !PasswordUtil.matches(req.password(), student.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResponse(false, "Invalid email or password.", null, null, null, null));
            }

            return ResponseEntity.ok(
                    new AuthResponse(true, "Login successful.", student.getStudentId(), student.getFullName(), "STUDENT", null));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponse(false, "Unexpected server error.", null, null, null, null));
        }
    }

    public static boolean isAdminSession(String token) {
        return token != null && ADMIN_SESSIONS.containsKey(token);
    }

    private String adminEmail() {
        return setting("ADMIN_EMAIL", "admin@cputfinder.local");
    }

    private String adminPasswordHash() {
        return setting("ADMIN_PASSWORD_HASH",
                "$2a$12$ZFZe.spF8K9CXHK1wELqGu6yEI6es8C1Dsb4FHWnpnoPb5xGQlDcu");
    }

    private String setting(String name, String defaultValue) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) value = System.getProperty(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
