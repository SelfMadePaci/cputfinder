package com.mycompany.cputfinder.web;

import com.mycompany.cputfinder.dao.RoomDAO;
import com.mycompany.cputfinder.dao.RoomDAOImpl;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.Room;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1", "http://localhost:80", "http://127.0.0.1:80"})
public class RoomController {
    private final RoomDAO roomDAO = new RoomDAOImpl();
    public record RoomRequest(String roomNumber, int floorNumber, int buildingId) {}

    @GetMapping
    public ResponseEntity<List<Room>> readAll(@RequestHeader(value = "X-Admin-Session", required = false) String token)
            throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(roomDAO.readAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RoomRequest request,
            @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (invalid(request)) return ResponseEntity.badRequest().body("Room number and a valid building are required.");
        Room room = new Room(0, request.roomNumber().trim(), request.floorNumber(), request.buildingId());
        if (!roomDAO.create(room)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Room already exists in this building.");
        return ResponseEntity.status(HttpStatus.CREATED).body(roomDAO.readAll().stream()
                .filter(item -> item.getRoomNumber().equals(room.getRoomNumber()) && item.getBuildingId() == room.getBuildingId())
                .findFirst().orElse(room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody RoomRequest request,
            @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (invalid(request)) return ResponseEntity.badRequest().body("Room number and a valid building are required.");
        Room room = new Room(id, request.roomNumber().trim(), request.floorNumber(), request.buildingId());
        if (!roomDAO.update(room)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(roomDAO.read(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id,
            @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        return roomDAO.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private boolean invalid(RoomRequest request) {
        return request == null || request.roomNumber() == null || request.roomNumber().isBlank()
                || request.floorNumber() < 0 || request.buildingId() <= 0;
    }
}
