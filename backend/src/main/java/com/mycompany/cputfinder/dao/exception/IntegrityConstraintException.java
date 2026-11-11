/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao.exception;

/**
 *
 * @author paci
 */
public class IntegrityConstraintException extends DatabaseException {
    //Thrown when a user attempts to save duplicate data, leaves a required field blank, or references a foreign key that does not exist (SQLState starting with 23).
    public IntegrityConstraintException(String message) {
        super(message);
    }

    public IntegrityConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}

