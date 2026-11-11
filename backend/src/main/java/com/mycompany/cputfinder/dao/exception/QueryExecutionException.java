/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao.exception;

/**
 *
 * @author paci
 */
public class QueryExecutionException extends DatabaseException {
    //Thrown for syntax errors, missing tables, or general execution failures (SQLState starting with 42 or 22).
    public QueryExecutionException(String message) {
        super(message);
    }

    public QueryExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
}