/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao.exception;

/**
 *
 * @author paci
 */
public class RecordNotFoundException extends DatabaseException {
    //Thrown when a SELECT, UPDATE, or DELETE query executes successfully but affects zero rows (e.g., trying to delete a schedule ID that is not in the database).
    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}