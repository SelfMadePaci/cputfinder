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
import com.mycompany.cputfinder.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAOImpl {

    // CREATE: Register a new user
    public boolean registerUser(String username, String plainPassword) {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, hashedPassword);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    // READ: Authenticate user login credentials
    public boolean authenticateUser(String username, String plainPassword) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    return PasswordUtil.checkPassword(plainPassword, storedHash);
                }
            }
        } catch (SQLException e) {
            System.err.println("Authentication error: " + e.getMessage());
        }
        return false;
    }

    // UPDATE: Update user password
    public boolean updatePassword(String username, String newPlainPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE username = ?";
        String newHashedPassword = PasswordUtil.hashPassword(newPlainPassword);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newHashedPassword);
            stmt.setString(2, username);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Password update error: " + e.getMessage());
            return false;
        }
    }

    // DELETE: Remove user account
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("User deletion error: " + e.getMessage());
            return false;
        }
    }
}
