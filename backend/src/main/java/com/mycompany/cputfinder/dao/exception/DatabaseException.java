/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao.exception;

import java.sql.SQLException;

/**
 *
 * @author paci
 */
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
public static DatabaseException wrap(SQLException e) {
    String sqlState = e.getSQLState();

    if (sqlState != null) {
        if (sqlState.startsWith("08")) {
            return new DatabaseConnectionException("Cannot reach the database server.", e);
        } else if (sqlState.startsWith("23")) {
            return new IntegrityConstraintException("Data conflict: Check for duplicate keys or missing foreign key references.", e);
        } else if (sqlState.startsWith("42") || sqlState.startsWith("22")) {
                return new QueryExecutionException("A query syntax or data format error occurred.", e);
        }
    }
        
        return new DatabaseException("An unexpected database error occurred: " + e.getMessage(), e);
    }
}
    
