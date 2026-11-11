package com.mycompany.cputfinder.web;

import com.mycompany.cputfinder.dao.LecturerDAO;
import com.mycompany.cputfinder.dao.LecturerDAOImpl;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.Lecturer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lecturers")
@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1", "http://localhost:80", "http://127.0.0.1:80"})
public class LecturerController {
    private final LecturerDAO lecturerDAO = new LecturerDAOImpl();
    public record LecturerRequest(String firstName, String lastName, String email, int officeId) {}

    @GetMapping
    public ResponseEntity<List<Lecturer>> readAll(@RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(lecturerDAO.readAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody LecturerRequest request, @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (invalid(request)) return ResponseEntity.badRequest().body("First name, last name, and email are required.");
        Lecturer lecturer = new Lecturer(0, request.firstName().trim(), request.lastName().trim(), request.email().trim(), request.officeId());
        if (!lecturerDAO.create(lecturer)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Lecturer email already exists.");
        return ResponseEntity.status(HttpStatus.CREATED).body(lecturer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody LecturerRequest request, @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (invalid(request)) return ResponseEntity.badRequest().body("First name, last name, and email are required.");
        Lecturer lecturer = new Lecturer(id, request.firstName().trim(), request.lastName().trim(), request.email().trim(), request.officeId());
        if (!lecturerDAO.update(lecturer)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(lecturerDAO.read(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id, @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        return lecturerDAO.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private boolean invalid(LecturerRequest request) {
        return request == null || blank(request.firstName()) || blank(request.lastName()) || blank(request.email());
    }
    private boolean blank(String value) { return value == null || value.isBlank(); }
}
