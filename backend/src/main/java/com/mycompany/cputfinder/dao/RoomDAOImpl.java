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
import com.mycompany.cputfinder.domain.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomDAOImpl implements RoomDAO {

    private static final String BASE_SELECT = 
        "SELECT r.room_id, r.room_number, r.floor_number, r.building_id, b.building_name " +
        "FROM Room r LEFT JOIN Building b ON r.building_id = b.building_id ";

    @Override
    public boolean create(Room room) throws DatabaseException {
        String sql = "INSERT INTO Room (room_number, floor_number, building_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getFloorNumber());
            stmt.setInt(3, room.getBuildingId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public Room read(int roomId) throws DatabaseException {
        String sql = BASE_SELECT + "WHERE r.room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractRoomFromResultSet(rs);
                } else {
                    throw new RecordNotFoundException("Room with ID " + roomId + " was not found.");
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<Room> readAll() throws DatabaseException {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(BASE_SELECT);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rooms.add(extractRoomFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
        return rooms;
    }

    @Override
    public boolean update(Room room) throws DatabaseException {
        String sql = "UPDATE Room SET room_number = ?, floor_number = ?, building_id = ? WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, room.getRoomNumber());
            stmt.setInt(2, room.getFloorNumber());
            stmt.setInt(3, room.getBuildingId());
            stmt.setInt(4, room.getRoomId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Update failed: Room with ID " + room.getRoomId() + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public boolean delete(int roomId) throws DatabaseException {
        String sql = "DELETE FROM Room WHERE room_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, roomId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RecordNotFoundException("Delete failed: Room with ID " + roomId + " does not exist.");
            }
            return true;
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
    }

    @Override
    public List<Room> searchByKeyword(String keyword) throws DatabaseException {
        List<Room> rooms = new ArrayList<>();
        String sql = BASE_SELECT + 
                     "WHERE LOWER(r.room_number) LIKE ? OR LOWER(b.building_name) LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword.toLowerCase().trim() + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(extractRoomFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw DatabaseException.wrap(e);
        }
        return rooms;
    }

    private Room extractRoomFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("room_id");
        String number = rs.getString("room_number");
        int floor = rs.getInt("floor_number");
        int bId = rs.getInt("building_id");
        String bName = rs.getString("building_name");
        return new Room(id, number, floor, bId, bName);
    }
}