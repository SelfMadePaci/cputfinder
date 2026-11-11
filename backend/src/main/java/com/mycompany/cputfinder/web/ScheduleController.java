package com.mycompany.cputfinder.web;

import com.mycompany.cputfinder.dao.ScheduleDAO;
import com.mycompany.cputfinder.dao.ScheduleDAOImpl;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.Schedule;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@CrossOrigin(origins = {"http://localhost", "http://127.0.0.1", "http://localhost:80", "http://127.0.0.1:80"})
public class ScheduleController {
    private final ScheduleDAO scheduleDAO = new ScheduleDAOImpl();
    public record ScheduleRequest(int studentId, int courseId, int roomId, String classDate, String startingTime, String endingTime) {}

    @GetMapping
    public ResponseEntity<List<Schedule>> readAll(@RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        return ResponseEntity.ok(scheduleDAO.readAll());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Schedule>> readForStudent(@PathVariable int studentId,
            @RequestHeader(value = "X-Student-Id", required = false) String requestedStudent) throws DatabaseException {
        if (requestedStudent == null || !requestedStudent.equals(String.valueOf(studentId))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(scheduleDAO.findByStudentId(studentId));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ScheduleRequest request,
            @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        try {
            Schedule schedule = toSchedule(0, request);
            if (!scheduleDAO.create(schedule)) return ResponseEntity.status(HttpStatus.CONFLICT).body("Schedule could not be created.");
            return ResponseEntity.status(HttpStatus.CREATED).body(scheduleDAO.readAll().stream()
                    .filter(item -> item.getStudentId() == schedule.getStudentId()
                            && item.getCourseId() == schedule.getCourseId()
                            && item.getClassDate().equals(schedule.getClassDate())
                            && item.getStartingTime().equals(schedule.getStartingTime()))
                    .findFirst().orElse(schedule));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Use valid student, course, room, date, and time values.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody ScheduleRequest request,
            @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        try {
            Schedule schedule = toSchedule(id, request);
            if (!scheduleDAO.update(schedule)) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(scheduleDAO.read(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Use valid student, course, room, date, and time values.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id,
            @RequestHeader(value = "X-Admin-Session", required = false) String token) throws DatabaseException {
        if (!AuthController.isAdminSession(token)) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin access required.");
        return scheduleDAO.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    private Schedule toSchedule(int id, ScheduleRequest request) {
        if (request == null || request.studentId() <= 0 || request.courseId() <= 0 || request.roomId() <= 0) throw new IllegalArgumentException();
        return new Schedule(id, request.studentId(), request.courseId(), request.roomId(),
                Date.valueOf(request.classDate()), Time.valueOf(request.startingTime()), Time.valueOf(request.endingTime()));
    }
}
