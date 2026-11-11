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
import com.mycompany.cputfinder.domain.Schedule;
import java.util.List;

public interface ScheduleDAO {
    boolean create(Schedule schedule) throws DatabaseException;
    Schedule read(int scheduleId) throws DatabaseException;
    List<Schedule> readAll() throws DatabaseException;
    boolean update(Schedule schedule) throws DatabaseException;
    boolean delete(int scheduleId) throws DatabaseException;
    List<Schedule> findByStudentId(int studentId) throws DatabaseException;
}

