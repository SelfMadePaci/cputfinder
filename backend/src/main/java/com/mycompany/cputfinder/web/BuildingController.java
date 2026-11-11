package com.mycompany.cputfinder.web;

import com.mycompany.cputfinder.dao.BuildingDAO;
import com.mycompany.cputfinder.dao.BuildingDAOImpl;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.Building;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buildings")
@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1", "http://localhost:80", "http://127.0.0.1:80"})
public class BuildingController {
    private final BuildingDAO buildingDAO = new BuildingDAOImpl();

    public record BuildingRequest(String buildingName, String buildingCode, String description) {}

    @GetMapping
    public ResponseEntity<List<Building>> readAll(
            @RequestHeader(value = "X-Admin-Session", required = false) String token,
            @RequestHeader(value = "X-Student-Id", required = false) String studentId)
            throws DatabaseException {
        if (!AuthController.isAdminSession(token) && (studentId == null || studentId.isBlank())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(buildingDAO.readAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody BuildingRequest request,
            @RequestHeader(value = "X-Admin-Session", required = false) String token)
            throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (invalid(request)) return ResponseEntity.badRequest().body("Building name, code, and description are required.");
        Building building = new Building(0, request.buildingName().trim(), request.buildingCode().trim(), request.description().trim());
        if (!buildingDAO.create(building)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Building code already exists.");
        return ResponseEntity.status(HttpStatus.CREATED).body(building);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody BuildingRequest request,
            @RequestHeader(value = "X-Admin-Session", required = false) String token)
            throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (invalid(request)) return ResponseEntity.badRequest().body("Building name, code, and description are required.");
        Building building = new Building(id, request.buildingName().trim(), request.buildingCode().trim(), request.description().trim());
        if (!buildingDAO.update(building)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(building);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id,
            @RequestHeader(value = "X-Admin-Session", required = false) String token)
            throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        return buildingDAO.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private boolean invalid(BuildingRequest request) {
        return request == null || blank(request.buildingName()) || blank(request.buildingCode()) || blank(request.description());
    }

    private boolean blank(String value) {
        return value == null || value.isBlank();
    }
}
