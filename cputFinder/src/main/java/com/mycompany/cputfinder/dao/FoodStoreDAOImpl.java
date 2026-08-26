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
import com.mycompany.cputfinder.domain.FoodStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodStoreDAOImpl implements FoodStoreDAO {

    @Override
    public boolean create(FoodStore store) {
        String sql = "INSERT INTO Food_Store (store_name, operating_hours, food_type, building_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, store.getStoreName());
            stmt.setString(2, store.getOperatingHours());
            stmt.setString(3, store.getFoodType());
            stmt.setInt(4, store.getBuildingId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public FoodStore read(int storeId) {
        String sql = "SELECT fs.store_id, fs.store_name, fs.operating_hours, fs.food_type, fs.building_id, b.building_name " +
                     "FROM Food_Store fs LEFT JOIN Building b ON fs.building_id = b.building_id " +
                     "WHERE fs.store_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, storeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractFoodStoreFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FoodStore> readAll() {
        List<FoodStore> stores = new ArrayList<>();
        String sql = "SELECT fs.store_id, fs.store_name, fs.operating_hours, fs.food_type, fs.building_id, b.building_name " +
                     "FROM Food_Store fs LEFT JOIN Building b ON fs.building_id = b.building_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                stores.add(extractFoodStoreFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stores;
    }

    @Override
    public boolean update(FoodStore store) {
        String sql = "UPDATE Food_Store SET store_name = ?, operating_hours = ?, food_type = ?, building_id = ? WHERE store_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, store.getStoreName());
            stmt.setString(2, store.getOperatingHours());
            stmt.setString(3, store.getFoodType());
            stmt.setInt(4, store.getBuildingId());
            stmt.setInt(5, store.getStoreId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int storeId) {
        String sql = "DELETE FROM Food_Store WHERE store_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, storeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<FoodStore> searchByKeyword(String keyword) {
        List<FoodStore> stores = new ArrayList<>();
        String sql = "SELECT fs.store_id, fs.store_name, fs.operating_hours, fs.food_type, fs.building_id, b.building_name " +
                     "FROM Food_Store fs LEFT JOIN Building b ON fs.building_id = b.building_id " +
                     "WHERE LOWER(fs.store_name) LIKE ? OR LOWER(fs.food_type) LIKE ? OR LOWER(b.building_name) LIKE ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword.toLowerCase().trim() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stores.add(extractFoodStoreFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stores;
    }

    private FoodStore extractFoodStoreFromResultSet(ResultSet rs) throws SQLException {
        return new FoodStore(
            rs.getInt("store_id"),
            rs.getString("store_name"),
            rs.getString("operating_hours"),
            rs.getString("food_type"),
            rs.getInt("building_id"),
            rs.getString("building_name")
        );
    }
}
