package com.mycompany.cputfinder.web;

import com.mycompany.cputfinder.dao.StudentDAO;
import com.mycompany.cputfinder.dao.StudentDAOImpl;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.Student;
import com.mycompany.cputfinder.util.PasswordUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1", "http://localhost:80", "http://127.0.0.1:80"})
public class StudentController {

    private final StudentDAO studentDAO = new StudentDAOImpl();

    public record StudentRequest(
            String studentNumber,
            String fullName,
            String studentEmail,
            String phoneNumber,
            String password) {}

    public record StudentResponse(
            int studentId,
            String studentNumber,
            String fullName,
            String studentEmail,
            String phoneNumber) {}

    @GetMapping
    public ResponseEntity<List<StudentResponse>> readAll(
            @RequestHeader(value = "X-Admin-Session", required = false) String adminToken)
            throws DatabaseException {
        if (!AuthController.isAdminSession(adminToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(studentDAO.readAll().stream().map(this::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> read(@PathVariable int id,
            @RequestHeader(value = "X-Admin-Session", required = false) String adminToken,
            @RequestHeader(value = "X-Student-Id", required = false) String requestedStudent)
            throws DatabaseException {
        if (!AuthController.isAdminSession(adminToken) && !ownsStudent(id, requestedStudent)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Student student = studentDAO.read(id);
        return student == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(toResponse(student));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StudentRequest request,
            @RequestHeader(value = "X-Admin-Session", required = false) String adminToken)
            throws DatabaseException {
        if (!AuthController.isAdminSession(adminToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (!valid(request, true)) {
            return ResponseEntity.badRequest().body("All student fields are required.");
        }

        Student student = new Student(
                request.studentNumber(),
                request.fullName(),
                request.studentEmail(),
                PasswordUtil.hash(request.password()));
        student.setPhoneNumber(request.phoneNumber());

        if (!studentDAO.create(student)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("The student number or email already exists.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody StudentRequest request,
            @RequestHeader(value = "X-Admin-Session", required = false) String adminToken,
            @RequestHeader(value = "X-Student-Id", required = false) String requestedStudent)
            throws DatabaseException {
        if (!AuthController.isAdminSession(adminToken) && !ownsStudent(id, requestedStudent)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You may only update your own profile.");
        }
        if (!valid(request, false)) {
            return ResponseEntity.badRequest().body("Student number, name, and email are required.");
        }

        Student existing = studentDAO.read(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        existing.setStudentNumber(request.studentNumber());
        existing.setFullName(request.fullName());
        existing.setStudentEmail(request.studentEmail());
        if (request.phoneNumber() != null) {
            existing.setPhoneNumber(request.phoneNumber());
        }
        if (request.password() != null && !request.password().isBlank()) {
            existing.setPassword(PasswordUtil.hash(request.password()));
        }

        if (!studentDAO.update(existing)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("The student number or email already exists.");
        }

        return ResponseEntity.ok(toResponse(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id,
            @RequestHeader(value = "X-Admin-Session", required = false) String adminToken)
            throws DatabaseException {
        if (!AuthController.isAdminSession(adminToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (studentDAO.read(id) == null) {
            return ResponseEntity.notFound().build();
        }

        return studentDAO.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("The student could not be deleted.");
    }

    private boolean valid(StudentRequest request, boolean passwordRequired) {
        return request != null
                && !isBlank(request.studentNumber())
                && !isBlank(request.fullName())
                && !isBlank(request.studentEmail())
                && (!passwordRequired || !isBlank(request.password()));
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private boolean ownsStudent(int id, String requestedStudent) {
        try {
            return requestedStudent != null && id == Integer.parseInt(requestedStudent);
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    private StudentResponse toResponse(Student student) {
        return new StudentResponse(
                student.getStudentId(),
                student.getStudentNumber(),
                student.getFullName(),
                student.getStudentEmail(),
                student.getPhoneNumber());
    }
}
