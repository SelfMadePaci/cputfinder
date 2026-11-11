package com.mycompany.cputfinder.web;

import com.mycompany.cputfinder.dao.SavedPlaceDAO;
import com.mycompany.cputfinder.dao.SavedPlaceDAOImpl;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.SavedPlace;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/saved-places")
@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1", "http://localhost:80", "http://127.0.0.1:80"})
public class SavedPlaceController {
    private final SavedPlaceDAO savedPlaceDAO = new SavedPlaceDAOImpl();
    public record SavedPlaceRequest(int studentId, int buildingId, String savedName) {}

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<SavedPlace>> byStudent(@PathVariable int studentId,
            @RequestHeader(value = "X-Student-Id", required = false) String requestedStudent,
            @RequestHeader(value = "X-Admin-Session", required = false) String adminToken)
            throws DatabaseException {
        if (!AuthController.isAdminSession(adminToken)
                && (requestedStudent == null || !requestedStudent.equals(String.valueOf(studentId)))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(savedPlaceDAO.findByStudentId(studentId));
    }

    @GetMapping
    public ResponseEntity<List<SavedPlace>> all(
            @RequestHeader(value = "X-Admin-Session", required = false) String adminToken)
            throws DatabaseException {
        if (!AuthController.isAdminSession(adminToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(savedPlaceDAO.readAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody SavedPlaceRequest request,
            @RequestHeader(value = "X-Student-Id", required = false) String requestedStudent,
            @RequestHeader(value = "X-Admin-Session", required = false) String adminToken)
            throws DatabaseException {
        if (!allowed(request, requestedStudent, adminToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You may only manage your own saved places.");
        if (invalid(request)) return ResponseEntity.badRequest().body("Student, building, and saved name are required.");
        SavedPlace place = new SavedPlace(request.studentId(), request.buildingId(), request.savedName().trim());
        if (!savedPlaceDAO.create(place)) return ResponseEntity.status(HttpStatus.CONFLICT).body("This building is already saved.");
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlaceDAO.findByStudentId(request.studentId()).stream()
                .filter(item -> item.getBuildingId() == request.buildingId()).findFirst().orElse(place));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id,
            @RequestHeader(value = "X-Student-Id", required = false) String requestedStudent,
            @RequestHeader(value = "X-Admin-Session", required = false) String adminToken)
            throws DatabaseException {
        SavedPlace existing = savedPlaceDAO.read(id);
        if (!AuthController.isAdminSession(adminToken)
                && (requestedStudent == null || !requestedStudent.equals(String.valueOf(existing.getStudentId())))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You may only manage your own saved places.");
        }
        return savedPlaceDAO.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private boolean allowed(SavedPlaceRequest request, String student, String adminToken) {
        return AuthController.isAdminSession(adminToken)
                || (request != null && student != null && student.equals(String.valueOf(request.studentId())));
    }
    private boolean invalid(SavedPlaceRequest request) {
        return request == null || request.studentId() <= 0 || request.buildingId() <= 0
                || request.savedName() == null || request.savedName().isBlank();
    }
}
