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
    public boolean create(SavedPlace savedPlace) throws DatabaseException {
        String sql = "INSERT INTO Saved_Place (student_id, building_id, saved_name) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, savedPlace.getStudentId());
            stmt.setInt(2, savedPlace.getBuildingId());
            stmt.setString(3, savedPlace.getSavedName());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public SavedPlace read(int savedId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE sp.saved_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, savedId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractSavedPlaceFromResultSet(rs);
                } else {
                    throw new RecordNotFoundException("Saved place with ID " + savedId + " was not found.");
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<SavedPlace> readAll() throws DatabaseException {
        List<SavedPlace> savedPlaces = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BASE_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                savedPlaces.add(extractSavedPlaceFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
        return savedPlaces;
    }

    @Override
    public boolean update(SavedPlace savedPlace) throws DatabaseException {
        String sql = "UPDATE Saved_Place SET student_id = ?, building_id = ?, saved_name = ? WHERE saved_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, savedPlace.getStudentId());
            stmt.setInt(2, savedPlace.getBuildingId());
            stmt.setString(3, savedPlace.getSavedName());
            stmt.setInt(4, savedPlace.getSavedId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Update failed: Saved place with ID " + savedPlace.getSavedId() + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public boolean delete(int savedId) throws DatabaseException {
        String sql = "DELETE FROM Saved_Place WHERE saved_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, savedId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Delete failed: Saved place with ID " + savedId + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<SavedPlace> findByStudentId(int studentId) throws DatabaseException {
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
            throw DatabaseException.wrap(e);
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