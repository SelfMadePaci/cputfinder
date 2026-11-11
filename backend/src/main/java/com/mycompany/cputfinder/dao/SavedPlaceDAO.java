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
import com.mycompany.cputfinder.domain.SavedPlace;
import java.util.List;

public interface SavedPlaceDAO {
    boolean create(SavedPlace savedPlace) throws DatabaseException;
    SavedPlace read(int savedId) throws DatabaseException;
    List<SavedPlace> readAll() throws DatabaseException;
    boolean update(SavedPlace savedPlace) throws DatabaseException;
    boolean delete(int savedId) throws DatabaseException;

    List<SavedPlace> findByStudentId(int studentId) throws DatabaseException;
}
