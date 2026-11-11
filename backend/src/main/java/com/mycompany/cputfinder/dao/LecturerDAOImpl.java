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
import com.mycompany.cputfinder.dao.exception.*;
import com.mycompany.cputfinder.domain.Lecturer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAOImpl implements LecturerDAO {

    private static final String BASE_SELECT = 
        "SELECT l.lecturer_id, l.first_name, l.last_name, l.email, l.office_id, o.office_number " +
        "FROM Lecturer l LEFT JOIN Office o ON l.office_id = o.office_id ";

    @Override
    public boolean create(Lecturer lecturer) throws DatabaseException {
        String sql = "INSERT INTO Lecturer (first_name, last_name, email, office_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lecturer.getFirstName());
            stmt.setString(2, lecturer.getLastName());
            stmt.setString(3, lecturer.getEmail());
            setOfficeId(stmt, 4, lecturer.getOfficeId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public Lecturer read(int lecturerId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE l.lecturer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lecturerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractLecturerFromResultSet(rs);
                } else {
                    throw new RecordNotFoundException("Lecturer with ID " + lecturerId + " was not found.");
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<Lecturer> readAll() throws DatabaseException {
        List<Lecturer> lecturers = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BASE_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lecturers.add(extractLecturerFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
        return lecturers;
    }

    @Override
    public boolean update(Lecturer lecturer) throws DatabaseException {
        String sql = "UPDATE Lecturer SET first_name = ?, last_name = ?, email = ?, office_id = ? WHERE lecturer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, lecturer.getFirstName());
            stmt.setString(2, lecturer.getLastName());
            stmt.setString(3, lecturer.getEmail());
            setOfficeId(stmt, 4, lecturer.getOfficeId());
            stmt.setInt(5, lecturer.getLecturerId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Update failed: Lecturer with ID " + lecturer.getLecturerId() + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public boolean delete(int lecturerId) throws DatabaseException {
        String sql = "DELETE FROM Lecturer WHERE lecturer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lecturerId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Delete failed: Lecturer with ID " + lecturerId + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<Lecturer> searchByKeyword(String keyword) throws DatabaseException {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = BASE_SELECT + 
                     "WHERE LOWER(l.first_name) LIKE ? OR LOWER(l.last_name) LIKE ? OR LOWER(l.email) LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword.toLowerCase().trim() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lecturers.add(extractLecturerFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
        return lecturers;
    }

    private Lecturer extractLecturerFromResultSet(ResultSet rs) throws SQLException {
        Lecturer lecturer = new Lecturer(
            rs.getInt("lecturer_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getInt("office_id")
        );
        lecturer.setOfficeNumber(rs.getString("office_number"));
        return lecturer;
    }

    private void setOfficeId(PreparedStatement statement, int parameterIndex, int officeId)
            throws SQLException {
        if (officeId > 0) {
            statement.setInt(parameterIndex, officeId);
        } else {
            statement.setNull(parameterIndex, java.sql.Types.INTEGER);
        }
    }
}