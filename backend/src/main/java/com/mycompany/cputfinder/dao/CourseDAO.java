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
import com.mycompany.cputfinder.domain.Course;
import java.util.List;

public interface CourseDAO {
    boolean create(Course course) throws DatabaseException;
    Course read(int courseId) throws DatabaseException;
    List<Course> readAll() throws DatabaseException;
    boolean update(Course course) throws DatabaseException;
    boolean delete(int courseId) throws DatabaseException;
    List<Course> searchByKeyword(String keyword) throws DatabaseException;
}
