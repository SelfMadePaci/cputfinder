/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.dao.exception.RecordNotFoundException;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.connection.DBConnection;
import com.mycompany.cputfinder.domain.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseDAOImpl implements CourseDAO {

    private static final String BASE_SELECT = 
    "SELECT DISTINCT c.course_id, c.course_code, c.course_name, c.lecturer_id, " +
    "CONCAT(l.first_name, ' ', l.last_name) AS lecturer_name " +
    "FROM Course c LEFT JOIN Lecturer l ON c.lecturer_id = l.lecturer_id ";
    
    @Override
    public boolean create(Course course) throws DatabaseException {
        String sql = "INSERT INTO Course (course_code, course_name, lecturer_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getLecturerId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public Course read(int courseId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE c.course_id = ?";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractCourseFromResultSet(rs);
            } else {
                throw new RecordNotFoundException("Course with ID " + courseId + " was not found.");
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

@Override
public List<Course> readAll() throws DatabaseException {
    List<Course> courses = new ArrayList<>();
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(BASE_SELECT);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            courses.add(extractCourseFromResultSet(rs));
        }
    } catch (SQLException e) {
        throw DatabaseException.wrap(e);
    }
    return courses;
}

    @Override
    public boolean update(Course course) throws DatabaseException {
        String sql = "UPDATE Course SET course_code = ?, course_name = ?, lecturer_id = ? WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getCourseName());
            stmt.setInt(3, course.getLecturerId());
            stmt.setInt(4, course.getCourseId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Update failed: Schedule with ID " + course.getCourseId() + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public boolean delete(int courseId) throws DatabaseException {
        String sql = "DELETE FROM Course WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, courseId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<Course> searchByKeyword(String keyword) throws DatabaseException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT c.course_id, c.course_code, c.course_name, c.lecturer_id, " +
                     "CONCAT(l.first_name, ' ', l.last_name) AS lecturer_name " +
                     "FROM Course c LEFT JOIN Lecturer l ON c.lecturer_id = l.lecturer_id " +
                     "WHERE LOWER(c.course_code) LIKE ? OR LOWER(c.course_name) LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + keyword.toLowerCase().trim() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    courses.add(extractCourseFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
        return courses;
    }

    private Course extractCourseFromResultSet(ResultSet rs) throws SQLException {
        Course course = new Course(
            rs.getInt("course_id"),
            rs.getString("course_code"),
            rs.getString("course_name"),
            rs.getInt("lecturer_id")
        );
        course.setLecturerName(rs.getString("lecturer_name"));
        return course;
    }
}
