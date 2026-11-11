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
import com.mycompany.cputfinder.domain.SavedPlace;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SavedPlaceDAOImpl implements SavedPlaceDAO {

    private static final String BASE_SELECT =
        "SELECT sp.saved_id, sp.student_id, sp.building_id, sp.saved_name, b.building_name " +
        "FROM Saved_Place sp " +
        "LEFT JOIN Building b ON sp.building_id = b.building_id ";

    @Override
    public boolean create(SavedPlace savedPlace) {
        String sql = "INSERT INTO Saved_Place (student_id, building_id, saved_name) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, savedPlace.getStudentId());
            stmt.setInt(2, savedPlace.getBuildingId());
            stmt.setString(3, savedPlace.getSavedName());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public SavedPlace read(int savedId) {
        String sql = BASE_SELECT + "WHERE sp.saved_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, savedId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractSavedPlaceFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<SavedPlace> readAll() {
        List<SavedPlace> savedPlaces = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BASE_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                savedPlaces.add(extractSavedPlaceFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return savedPlaces;
    }

    @Override
    public boolean update(SavedPlace savedPlace) {
        String sql = "UPDATE Saved_Place SET student_id = ?, building_id = ?, saved_name = ? WHERE saved_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, savedPlace.getStudentId());
            stmt.setInt(2, savedPlace.getBuildingId());
            stmt.setString(3, savedPlace.getSavedName());
            stmt.setInt(4, savedPlace.getSavedId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int savedId) {
        String sql = "DELETE FROM Saved_Place WHERE saved_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, savedId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<SavedPlace> findByStudentId(int studentId) {
        List<SavedPlace> savedPlaces = new ArrayList<>();
        String sql = BASE_SELECT + "WHERE sp.student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    savedPlaces.add(extractSavedPlaceFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return savedPlaces;
    }

    private SavedPlace extractSavedPlaceFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("saved_id");
        int studentId = rs.getInt("student_id");
        int buildingId = rs.getInt("building_id");
        String savedName = rs.getString("saved_name");
        String buildingName = rs.getString("building_name");
        return new SavedPlace(id, studentId, buildingId, savedName, buildingName);
    }
}
