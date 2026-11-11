/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

import com.mycompany.cputfinder.connection.DBconnection.DBConnection;
import com.mycompany.cputfinder.domain.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public boolean create(Student student) {
        String sql = "INSERT INTO Student (student_number, full_name, student_email, password) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, student.getStudentNumber());
            stmt.setString(2, student.getFullName());
            stmt.setString(3, student.getStudentEmail());
            stmt.setString(4, student.getPassword());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        student.setStudentId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Student read(int studentId) {
        String sql = "SELECT student_id, student_number, full_name, student_email, password " +
                     "FROM Student WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractStudentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> readAll() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT student_id, student_number, full_name, student_email, password FROM Student";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    @Override
    public boolean update(Student student) {
        String sql = "UPDATE Student SET student_number = ?, full_name = ?, student_email = ?, password = ? " +
                     "WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getStudentNumber());
            stmt.setString(2, student.getFullName());
            stmt.setString(3, student.getStudentEmail());
            stmt.setString(4, student.getPassword());
            stmt.setInt(5, student.getStudentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int studentId) {

        String sql = "DELETE FROM Student WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Student findByEmail(String studentEmail) {
        String sql = "SELECT student_id, student_number, full_name, student_email, password " +
                     "FROM Student WHERE student_email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, studentEmail);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractStudentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("student_id");
        String number = rs.getString("student_number");
        String name = rs.getString("full_name");
        String email = rs.getString("student_email");
        String password = rs.getString("password");
        return new Student(id, number, name, email, password);
    }
}