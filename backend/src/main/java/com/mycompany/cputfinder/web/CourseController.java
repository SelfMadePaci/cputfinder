package com.mycompany.cputfinder.web;

import com.mycompany.cputfinder.dao.CourseDAO;
import com.mycompany.cputfinder.dao.CourseDAOImpl;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.Course;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1", "http://localhost:80", "http://127.0.0.1:80"})
public class CourseController {
    private final CourseDAO courseDAO = new CourseDAOImpl();
    public record CourseRequest(String courseCode, String courseName, int lecturerId) {}

    @GetMapping
    public ResponseEntity<List<Course>> readAll(@RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(courseDAO.readAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CourseRequest request, @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (invalid(request)) return ResponseEntity.badRequest().body("Course code, name, and lecturer are required.");
        Course course = new Course(0, request.courseCode().trim(), request.courseName().trim(), request.lecturerId());
        if (!courseDAO.create(course)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Course code already exists.");
        return ResponseEntity.status(HttpStatus.CREATED).body(courseDAO.readAll().stream().filter(item -> item.getCourseCode().equals(course.getCourseCode())).findFirst().orElse(course));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody CourseRequest request, @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (invalid(request)) return ResponseEntity.badRequest().body("Course code, name, and lecturer are required.");
        Course course = new Course(id, request.courseCode().trim(), request.courseName().trim(), request.lecturerId());
        if (!courseDAO.update(course)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(courseDAO.read(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id, @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        return courseDAO.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private boolean invalid(CourseRequest request) {
        return request == null || request.courseCode() == null || request.courseCode().isBlank()
                || request.courseName() == null || request.courseName().isBlank() || request.lecturerId() <= 0;
    }
}
