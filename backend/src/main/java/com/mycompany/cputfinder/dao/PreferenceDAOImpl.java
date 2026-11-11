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
import com.mycompany.cputfinder.domain.Preference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreferenceDAOImpl implements PreferenceDAO {

    private static final String BASE_SELECT = 
        "SELECT preference_id, student_id, accessible_route, notification FROM Preference ";

    @Override
    public boolean create(Preference preference) throws DatabaseException {
        String sql = "INSERT INTO Preference (student_id, accessible_route, notification) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, preference.getStudentId());
            stmt.setString(2, preference.getAccessibleRoute());
            stmt.setString(3, preference.getNotification());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public Preference read(int preferenceId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE preference_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, preferenceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPreferenceFromResultSet(rs);
                } else {
                    throw new RecordNotFoundException("Preference record with ID " + preferenceId + " was not found.");
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<Preference> readAll() throws DatabaseException {
        List<Preference> preferences = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BASE_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                preferences.add(extractPreferenceFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
        return preferences;
    }

    @Override
    public boolean update(Preference preference) throws DatabaseException {
        String sql = "UPDATE Preference SET accessible_route = ?, notification = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, preference.getAccessibleRoute());
            stmt.setString(2, preference.getNotification());
            stmt.setInt(3, preference.getStudentId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Update failed: Preference for student ID " + preference.getStudentId() + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public boolean delete(int preferenceId) throws DatabaseException {
        String sql = "DELETE FROM Preference WHERE preference_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, preferenceId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Delete failed: Preference with ID " + preferenceId + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public Preference findByStudentId(int studentId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPreferenceFromResultSet(rs);
                } else {
                    throw new RecordNotFoundException("Preference for student ID " + studentId + " was not found.");
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    private Preference extractPreferenceFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("preference_id");
        int studentId = rs.getInt("student_id");
        String accessibleRoute = rs.getString("accessible_route");
        String notification = rs.getString("notification");
        return new Preference(id, studentId, accessibleRoute, notification);
    }
}