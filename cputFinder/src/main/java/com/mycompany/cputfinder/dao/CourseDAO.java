/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */

import com.mycompany.cputfinder.domain.Course;
import java.util.List;

public interface CourseDAO {
    boolean create(Course course);
    Course read(int courseId);
    List<Course> readAll();
    boolean update(Course course);
    boolean delete(int courseId);
    List<Course> searchByKeyword(String keyword);
}
