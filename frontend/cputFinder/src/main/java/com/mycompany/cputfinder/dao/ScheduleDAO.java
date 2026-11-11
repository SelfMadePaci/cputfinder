/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;
/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.domain.Schedule;
import java.util.List;

public interface ScheduleDAO {
    boolean create(Schedule schedule);
    Schedule read(int scheduleId);
    List<Schedule> readAll();
    boolean update(Schedule schedule);
    boolean delete(int scheduleId);

    List<Schedule> findByStudentId(int studentId);
}
