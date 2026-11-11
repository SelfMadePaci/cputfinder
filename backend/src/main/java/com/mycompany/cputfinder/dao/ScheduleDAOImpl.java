/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.cputfinder.dao;
/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.connection.DBConnection;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.dao.exception.RecordNotFoundException;
import com.mycompany.cputfinder.domain.Schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleDAOImpl implements ScheduleDAO {

    private static final String BASE_SELECT =
        "SELECT s.schedule_id, s.student_id, s.course_id, s.room_id, s.class_date, " +
        "s.starting_time, s.ending_time, c.course_code AS module_code, c.course_name AS module_name, r.room_number " +
        "FROM Schedule s " +
        "LEFT JOIN Course c ON s.course_id = c.course_id " +
        "LEFT JOIN Room r ON s.room_id = r.room_id ";

    @Override
    public boolean create(Schedule schedule) throws DatabaseException {
        String sql = "INSERT INTO Schedule (student_id, course_id, room_id, class_date, starting_time, ending_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, schedule.getStudentId());
            stmt.setInt(2, schedule.getCourseId());
            stmt.setInt(3, schedule.getRoomId());
            stmt.setDate(4, schedule.getClassDate());
            stmt.setTime(5, schedule.getStartingTime());
            stmt.setTime(6, schedule.getEndingTime());

            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
    } 
     
}

    @Override
    public Schedule read(int scheduleId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE s.schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractScheduleFromResultSet(rs);
                } else {
                    throw new RecordNotFoundException("Schedule with ID " + scheduleId + " was not found.");
                }
            }  
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }
    @Override
    public List<Schedule> readAll() throws DatabaseException {
        List<Schedule> schedules = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BASE_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                schedules.add(extractScheduleFromResultSet(rs));
            }
        } catch (SQLException e) {
           throw DatabaseException.wrap(e);
        }
        return schedules;
    }

    @Override
    public boolean update(Schedule schedule) throws DatabaseException {
        String sql = "UPDATE Schedule SET student_id = ?, course_id = ?, room_id = ?, " +
                     "class_date = ?, starting_time = ?, ending_time = ? WHERE schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, schedule.getStudentId());
            stmt.setInt(2, schedule.getCourseId());
            stmt.setInt(3, schedule.getRoomId());
            stmt.setDate(4, schedule.getClassDate());
            stmt.setTime(5, schedule.getStartingTime());
            stmt.setTime(6, schedule.getEndingTime());
            stmt.setInt(7, schedule.getScheduleId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Update failed: Schedule with ID " + schedule.getScheduleId() + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public boolean delete(int scheduleId) throws DatabaseException {
        String sql = "DELETE FROM Schedule WHERE schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Delete failed: Schedule with ID " + scheduleId + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<Schedule> findByStudentId(int studentId) throws DatabaseException {
        List<Schedule> schedules = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE s.student_id = ? ORDER BY s.class_date, s.starting_time";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(extractScheduleFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        } 
        return schedules;
    }

    private Schedule extractScheduleFromResultSet(ResultSet rs) throws SQLException {
        return new Schedule(
            rs.getInt("schedule_id"),
            rs.getInt("student_id"),
            rs.getInt("course_id"),
            rs.getInt("room_id"),
            rs.getDate("class_date"),
            rs.getTime("starting_time"),
            rs.getTime("ending_time"),
            rs.getString("module_code"),
            rs.getString("module_name"),
            rs.getString("room_number")
        );
    }
}
