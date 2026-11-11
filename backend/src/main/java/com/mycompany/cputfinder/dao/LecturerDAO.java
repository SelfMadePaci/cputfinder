/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */

import com.mycompany.cputfinder.domain.Lecturer;
import com.mycompany.cputfinder.dao.exception.*;
import java.util.List;

public interface LecturerDAO {
    boolean create(Lecturer lecturer) throws DatabaseException;
    Lecturer read(int lecturerId) throws DatabaseException;
    List<Lecturer> readAll() throws DatabaseException;
    boolean update(Lecturer lecturer) throws DatabaseException;
    boolean delete(int lecturerId) throws DatabaseException;
    List<Lecturer> searchByKeyword(String keyword) throws DatabaseException;
}
