/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;
/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.domain.Preference;
import com.mycompany.cputfinder.dao.exception.*;
import java.util.List;

public interface PreferenceDAO {
    boolean create(Preference preference) throws DatabaseException;
    Preference read(int preferenceId) throws DatabaseException;
    List<Preference> readAll() throws DatabaseException;
    boolean update(Preference preference) throws DatabaseException;
    boolean delete(int preferenceId) throws DatabaseException;

    Preference findByStudentId(int studentId) throws DatabaseException;
}
