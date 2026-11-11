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
import com.mycompany.cputfinder.domain.Office;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OfficeDAOImpl implements OfficeDAO {

    @Override
    public boolean create(Office office) {
        String sql = "INSERT INTO Office (office_number, floor_number, building_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, office.getOfficeNumber());
            stmt.setInt(2, office.getFloorNumber());
            stmt.setInt(3, office.getBuildingId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Office read(int officeId) {
        String sql = "SELECT o.office_id, o.office_number, o.floor_number, o.building_id, b.building_name " +
                     "FROM Office o LEFT JOIN Building b ON o.building_id = b.building_id " +
                     "WHERE o.office_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, officeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractOfficeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Office> readAll() {
        List<Office> offices = new ArrayList<>();
        String sql = "SELECT o.office_id, o.office_number, o.floor_number, o.building_id, b.building_name " +
                     "FROM Office o LEFT JOIN Building b ON o.building_id = b.building_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                offices.add(extractOfficeFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offices;
    }

    @Override
    public boolean update(Office office) {
        String sql = "UPDATE Office SET office_number = ?, floor_number = ?, building_id = ? WHERE office_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, office.getOfficeNumber());
            stmt.setInt(2, office.getFloorNumber());
            stmt.setInt(3, office.getBuildingId());
            stmt.setInt(4, office.getOfficeId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int officeId) {
        String sql = "DELETE FROM Office WHERE office_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, officeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Office> searchByKeyword(String keyword) {
        List<Office> offices = new ArrayList<>();
        String sql = "SELECT o.office_id, o.office_number, o.floor_number, o.building_id, b.building_name " +
                     "FROM Office o LEFT JOIN Building b ON o.building_id = b.building_id " +
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
            e.printStackTrace();
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
