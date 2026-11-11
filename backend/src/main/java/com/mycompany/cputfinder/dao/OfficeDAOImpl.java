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
import com.mycompany.cputfinder.domain.Office;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OfficeDAOImpl implements OfficeDAO {

    private static final String BASE_SELECT = 
        "SELECT o.office_id, o.office_number, o.floor_number, o.building_id, b.building_name " +
        "FROM Office o LEFT JOIN Building b ON o.building_id = b.building_id ";

    @Override
    public boolean create(Office office) throws DatabaseException {
        String sql = "INSERT INTO Office (office_number, floor_number, building_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, office.getOfficeNumber());
            stmt.setInt(2, office.getFloorNumber());
            stmt.setInt(3, office.getBuildingId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public Office read(int officeId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE o.office_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, officeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractOfficeFromResultSet(rs);
                } else {
                    throw new RecordNotFoundException("Office with ID " + officeId + " was not found.");
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<Office> readAll() throws DatabaseException {
        List<Office> offices = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BASE_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                offices.add(extractOfficeFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
        return offices;
    }

    @Override
    public boolean update(Office office) throws DatabaseException {
        String sql = "UPDATE Office SET office_number = ?, floor_number = ?, building_id = ? WHERE office_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, office.getOfficeNumber());
            stmt.setInt(2, office.getFloorNumber());
            stmt.setInt(3, office.getBuildingId());
            stmt.setInt(4, office.getOfficeId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Update failed: Office with ID " + office.getOfficeId() + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public boolean delete(int officeId) throws DatabaseException {
        String sql = "DELETE FROM Office WHERE office_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, officeId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Delete failed: Office with ID " + officeId + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<Office> searchByKeyword(String keyword) throws DatabaseException {
        List<Office> offices = new ArrayList<>();
        String sql = BASE_SELECT + 
                     "WHERE LOWER(o.office_number) LIKE ? OR LOWER(b.building_name) LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String pattern = "%" + keyword.toLowerCase().trim() + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    offices.add(extractOfficeFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
        return offices;
    }

    private Office extractOfficeFromResultSet(ResultSet rs) throws SQLException {
        Office office = new Office(
            rs.getInt("office_id"),
            rs.getString("office_number"),
            rs.getInt("floor_number"),
            rs.getInt("building_id")
        );
        office.setBuildingName(rs.getString("building_name"));
        return office;
    }
}