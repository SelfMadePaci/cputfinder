/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao.exception;

/**
 *
 * @author paci
 */
public class DatabaseConnectionException extends DatabaseException {
    //Thrown when the server is unreachable, timed out, or offline (SQLState starting with 08).

    /**
     *
     * @param message
     */
    public DatabaseConnectionException(String message) {
        super(message);
    }

    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}