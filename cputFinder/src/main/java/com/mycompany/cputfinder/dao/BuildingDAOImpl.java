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
import com.mycompany.cputfinder.domain.Building;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BuildingDAOImpl implements BuildingDAO {

    @Override
    public boolean create(Building building) {
        String sql = "INSERT INTO Building (building_name, building_code, description) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, building.getBuildingName());
            stmt.setString(2, building.getBuildingCode());
            stmt.setString(3, building.getDescription());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Building read(int buildingId) {
        String sql = "SELECT building_id, building_name, building_code, description FROM Building WHERE building_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, buildingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractBuildingFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Building> readAll() {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT building_id, building_name, building_code, description FROM Building";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                buildings.add(extractBuildingFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buildings;
    }

    @Override
    public boolean update(Building building) {
        String sql = "UPDATE Building SET building_name = ?, building_code = ?, description = ? WHERE building_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, building.getBuildingName());
            stmt.setString(2, building.getBuildingCode());
            stmt.setString(3, building.getDescription());
            stmt.setInt(4, building.getBuildingId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int buildingId) {
        String sql = "DELETE FROM Building WHERE building_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, buildingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Building> searchByKeyword(String keyword) {
        List<Building> buildings = new ArrayList<>();
        String sql = "SELECT building_id, building_name, building_code, description FROM Building " +
                     "WHERE LOWER(building_name) LIKE ? OR LOWER(building_code) LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword.toLowerCase().trim() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    buildings.add(extractBuildingFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return buildings;
    }

    private Building extractBuildingFromResultSet(ResultSet rs) throws SQLException {
        return new Building(
            rs.getInt("building_id"),
            rs.getString("building_name"),
            rs.getString("building_code"),
            rs.getString("description")
        );
    }
}
