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
import com.mycompany.cputfinder.domain.Route;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RouteDAOImpl implements RouteDAO {

    @Override
    public boolean create(Route route) {
        String sql = "INSERT INTO Route (start_building_id, end_building_id, estimated_time, description) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, route.getStartBuildingId());
            stmt.setInt(2, route.getEndBuildingId());
            stmt.setInt(3, route.getEstimatedTime());
            stmt.setString(4, route.getDescription());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Route read(int routeId) {
        String sql = "SELECT r.route_id, r.start_building_id, r.end_building_id, r.estimated_time, r.description, " +
                     "b1.building_name AS start_name, b2.building_name AS end_name " +
                     "FROM Route r " +
                     "LEFT JOIN Building b1 ON r.start_building_id = b1.building_id " +
                     "LEFT JOIN Building b2 ON r.end_building_id = b2.building_id " +
                     "WHERE r.route_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, routeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractRouteFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Route> readAll() {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT r.route_id, r.start_building_id, r.end_building_id, r.estimated_time, r.description, " +
                     "b1.building_name AS start_name, b2.building_name AS end_name " +
                     "FROM Route r " +
                     "LEFT JOIN Building b1 ON r.start_building_id = b1.building_id " +
                     "LEFT JOIN Building b2 ON r.end_building_id = b2.building_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                routes.add(extractRouteFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    public boolean update(Route route) {
        String sql = "UPDATE Route SET start_building_id = ?, end_building_id = ?, estimated_time = ?, description = ? WHERE route_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, route.getStartBuildingId());
            stmt.setInt(2, route.getEndBuildingId());
            stmt.setInt(3, route.getEstimatedTime());
            stmt.setString(4, route.getDescription());
            stmt.setInt(5, route.getRouteId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int routeId) {
        String sql = "DELETE FROM Route WHERE route_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, routeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Route> findRouteBetween(int startBuildingId, int endBuildingId) {
        List<Route> routes = new ArrayList<>();
        String sql = "SELECT r.route_id, r.start_building_id, r.end_building_id, r.estimated_time, r.description, " +
                     "b1.building_name AS start_name, b2.building_name AS end_name " +
                     "FROM Route r " +
                     "LEFT JOIN Building b1 ON r.start_building_id = b1.building_id " +
                     "LEFT JOIN Building b2 ON r.end_building_id = b2.building_id " +
                     "WHERE r.start_building_id = ? AND r.end_building_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, startBuildingId);
            stmt.setInt(2, endBuildingId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    routes.add(extractRouteFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    private Route extractRouteFromResultSet(ResultSet rs) throws SQLException {
        Route route = new Route(
            rs.getInt("route_id"),
            rs.getInt("start_building_id"),
            rs.getInt("end_building_id"),
            rs.getInt("estimated_time"),
            rs.getString("description")
        );
        route.setStartBuildingName(rs.getString("start_name"));
        route.setEndBuildingName(rs.getString("end_name"));
        return route;
    }
}