package com.mycompany.cputfinder.web;

import com.mycompany.cputfinder.dao.FoodStoreDAO;
import com.mycompany.cputfinder.dao.FoodStoreDAOImpl;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.FoodStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/food-stores")
@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1", "http://localhost:80", "http://127.0.0.1:80"})
public class FoodStoreController {
    private final FoodStoreDAO storeDAO = new FoodStoreDAOImpl();
    public record StoreRequest(String storeName, String operatingHours, String foodType, int buildingId) {}

    @GetMapping
    public ResponseEntity<List<FoodStore>> readAll(@RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(storeDAO.readAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody StoreRequest request, @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (invalid(request)) return ResponseEntity.badRequest().body("Store name, hours, food type, and building are required.");
        FoodStore store = new FoodStore(0, request.storeName().trim(), request.operatingHours().trim(), request.foodType().trim(), request.buildingId());
        if (!storeDAO.create(store)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Food store could not be created.");
        return ResponseEntity.status(HttpStatus.CREATED).body(storeDAO.readAll().stream().filter(item -> item.getStoreName().equals(store.getStoreName())).findFirst().orElse(store));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody StoreRequest request, @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        if (invalid(request)) return ResponseEntity.badRequest().body("Store name, hours, food type, and building are required.");
        FoodStore store = new FoodStore(id, request.storeName().trim(), request.operatingHours().trim(), request.foodType().trim(), request.buildingId());
        if (!storeDAO.update(store)) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(storeDAO.read(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id, @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        return storeDAO.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private boolean invalid(StoreRequest r) {
        return r == null || blank(r.storeName()) || blank(r.operatingHours()) || blank(r.foodType()) || r.buildingId() <= 0;
    }
    private boolean blank(String value) { return value == null || value.isBlank(); }
}
