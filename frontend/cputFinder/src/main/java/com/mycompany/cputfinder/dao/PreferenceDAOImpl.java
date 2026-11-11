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
import com.mycompany.cputfinder.domain.Preference;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreferenceDAOImpl implements PreferenceDAO {

    @Override
    public boolean create(Preference preference) {
        String sql = "INSERT INTO Preference (student_id, accessible_route, notification) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, preference.getStudentId());
            stmt.setString(2, preference.getAccessibleRoute());
            stmt.setString(3, preference.getNotification());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Preference read(int preferenceId) {
        String sql = "SELECT preference_id, student_id, accessible_route, notification " +
                     "FROM Preference WHERE preference_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, preferenceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPreferenceFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Preference> readAll() {
        List<Preference> preferences = new ArrayList<>();
        String sql = "SELECT preference_id, student_id, accessible_route, notification FROM Preference";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                preferences.add(extractPreferenceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return preferences;
    }

    @Override
    public boolean update(Preference preference) {
        String sql = "UPDATE Preference SET accessible_route = ?, notification = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, preference.getAccessibleRoute());
            stmt.setString(2, preference.getNotification());
            stmt.setInt(3, preference.getStudentId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int preferenceId) {
        String sql = "DELETE FROM Preference WHERE preference_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, preferenceId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Preference findByStudentId(int studentId) {
        String sql = "SELECT preference_id, student_id, accessible_route, notification " +
                     "FROM Preference WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractPreferenceFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Preference extractPreferenceFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("preference_id");
        int studentId = rs.getInt("student_id");
        String accessibleRoute = rs.getString("accessible_route");
        String notification = rs.getString("notification");
        return new Preference(id, studentId, accessibleRoute, notification);
    }
}
