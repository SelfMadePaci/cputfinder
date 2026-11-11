package com.mycompany.cputfinder.web;

import com.mycompany.cputfinder.dao.MenuItemDAO;
import com.mycompany.cputfinder.dao.MenuItemDAOImpl;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.MenuItem;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/menu-items")
@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1", "http://localhost:80", "http://127.0.0.1:80"})
public class MenuItemController {
    private static final Set<String> CATEGORIES = Set.of("Breakfast", "Lunch", "Dinner");
    private static final BigDecimal MAX_PRICE = new BigDecimal("999999.99");

    private final MenuItemDAO menuItemDAO = new MenuItemDAOImpl();

    public record MenuItemRequest(String category, String itemName, BigDecimal price, Integer displayOrder) {}

    @GetMapping
    public ResponseEntity<List<MenuItem>> readAll() throws DatabaseException {
        return ResponseEntity.ok(menuItemDAO.readAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody MenuItemRequest request,
            @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        }
        if (invalid(request)) {
            return ResponseEntity.badRequest().body("Choose a meal, enter an item name and valid price/order.");
        }

        MenuItem menuItem = toMenuItem(0, request);
        if (!menuItemDAO.create(menuItem)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Menu item could not be created.");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody MenuItemRequest request,
            @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        }
        if (id <= 0 || invalid(request)) {
            return ResponseEntity.badRequest().body("Choose a meal, enter an item name and valid price/order.");
        }

        MenuItem menuItem = toMenuItem(id, request);
        if (!menuItemDAO.update(menuItem)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(menuItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id,
            @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        }
        return menuItemDAO.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private MenuItem toMenuItem(int id, MenuItemRequest request) {
        String category = CATEGORIES.stream()
                .filter(value -> value.equalsIgnoreCase(request.category().trim()))
                .findFirst()
                .orElseThrow();
        return new MenuItem(id, category, request.itemName().trim(), request.price(), request.displayOrder());
    }

    private boolean invalid(MenuItemRequest request) {
        return request == null
                || request.category() == null
                || CATEGORIES.stream().noneMatch(value -> value.equalsIgnoreCase(request.category().trim()))
                || request.itemName() == null
                || request.itemName().isBlank()
                || request.itemName().trim().length() > 150
                || request.price() == null
                || request.price().compareTo(BigDecimal.ZERO) < 0
                || request.price().compareTo(MAX_PRICE) > 0
                || request.price().scale() > 2
                || request.displayOrder() == null
                || request.displayOrder() < 0;
    }
}
