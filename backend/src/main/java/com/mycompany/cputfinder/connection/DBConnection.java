/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author paci
 */
public class DBConnection {

    public static Connection getConnection() throws SQLException {
        String host = setting("DB_HOST", "localhost");
        String port = setting("DB_PORT", "3306");
        String database = setting("DB_NAME", "cput_finder");
        String username = setting("DB_USER", "root");
        String password = setting("DB_PASSWORD", "");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + database
                + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        return DriverManager.getConnection(url, username, password);
    }

    private static String setting(String name, String defaultValue) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            value = System.getProperty(name);
        }
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
