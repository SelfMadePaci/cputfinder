/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.cputfinder.dao;
/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.connection.DBconnection.DBConnection;
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
        "s.starting_time, s.ending_time, c.module_code, c.module_name, r.room_number " +
        "FROM Schedule s " +
        "LEFT JOIN Course c ON s.course_id = c.course_id " +
        "LEFT JOIN Room r ON s.room_id = r.room_id ";

    @Override
    public boolean create(Schedule schedule) {
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
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Schedule read(int scheduleId) {
        String sql = BASE_SELECT + "WHERE s.schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractScheduleFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Schedule> readAll() {
        List<Schedule> schedules = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BASE_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                schedules.add(extractScheduleFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return schedules;
    }

    @Override
    public boolean update(Schedule schedule) {
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

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int scheduleId) {
        String sql = "DELETE FROM Schedule WHERE schedule_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Schedule> findByStudentId(int studentId) {
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
            e.printStackTrace();
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
