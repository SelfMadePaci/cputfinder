/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */

import com.mycompany.cputfinder.dao.exception.*;
import com.mycompany.cputfinder.domain.Office;
import java.util.List;

public interface OfficeDAO {
    boolean create(Office office) throws DatabaseException;
    Office read(int officeId) throws DatabaseException;
    List<Office> readAll() throws DatabaseException;
    boolean update(Office office) throws DatabaseException;
    boolean delete(int officeId) throws DatabaseException;
    List<Office> searchByKeyword(String keyword) throws DatabaseException;
}
