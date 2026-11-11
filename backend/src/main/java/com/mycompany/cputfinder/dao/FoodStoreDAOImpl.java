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
import com.mycompany.cputfinder.domain.FoodStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FoodStoreDAOImpl implements FoodStoreDAO {

    private static final String BASE_SELECT = 
        "SELECT fs.store_id, fs.store_name, fs.operating_hours, fs.food_type, fs.building_id, b.building_name " +
        "FROM Food_Store fs LEFT JOIN Building b ON fs.building_id = b.building_id ";

    @Override
    public boolean create(FoodStore store) throws DatabaseException {
        String sql = "INSERT INTO Food_Store (store_name, operating_hours, food_type, building_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, store.getStoreName());
            stmt.setString(2, store.getOperatingHours());
            stmt.setString(3, store.getFoodType());
            stmt.setInt(4, store.getBuildingId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public FoodStore read(int storeId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE fs.store_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, storeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractFoodStoreFromResultSet(rs);
                } else {
                    throw new RecordNotFoundException("Food store with ID " + storeId + " was not found.");
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<FoodStore> readAll() throws DatabaseException {
        List<FoodStore> stores = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BASE_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                stores.add(extractFoodStoreFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
        return stores;
    }

    @Override
    public boolean update(FoodStore store) throws DatabaseException {
        String sql = "UPDATE Food_Store SET store_name = ?, operating_hours = ?, food_type = ?, building_id = ? WHERE store_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, store.getStoreName());
            stmt.setString(2, store.getOperatingHours());
            stmt.setString(3, store.getFoodType());
            stmt.setInt(4, store.getBuildingId());
            stmt.setInt(5, store.getStoreId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Update failed: Food store with ID " + store.getStoreId() + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public boolean delete(int storeId) throws DatabaseException {
        String sql = "DELETE FROM Food_Store WHERE store_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, storeId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Delete failed: Food store with ID " + storeId + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<FoodStore> searchByKeyword(String keyword) throws DatabaseException {
        List<FoodStore> stores = new ArrayList<>();
        String sql = BASE_SELECT + 
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
            throw DatabaseException.wrap(e);
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