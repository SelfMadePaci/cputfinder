/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.dao.exception.DatabaseException;

public interface UserDAO {
    boolean registerUser(String username, String plainPassword) throws DatabaseException;
    boolean authenticateUser(String username, String plainPassword) throws DatabaseException;
    boolean updatePassword(String username, String newPlainPassword) throws DatabaseException;
    boolean deleteUser(String username) throws DatabaseException;
}